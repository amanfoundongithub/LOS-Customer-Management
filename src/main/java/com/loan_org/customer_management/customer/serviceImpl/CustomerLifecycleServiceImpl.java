package com.loan_org.customer_management.customer.serviceImpl;

import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerLifecycleService;
import com.loan_org.customer_management.event.publisher.CustomerEventPublisher;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidCustomerStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerLifecycleServiceImpl
        implements CustomerLifecycleService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerEventPublisher customerEventPublisher;

    @Override
    @Transactional
    public CustomerResponse activateCustomer(String customerId) {

        return changeStatus(
                customerId,
                CustomerStatus.ACTIVE
        );
    }

    @Override
    @Transactional
    public CustomerResponse suspendCustomer(String customerId) {

        return changeStatus(
                customerId,
                CustomerStatus.SUSPENDED
        );
    }

    @Override
    @Transactional
    public CustomerResponse deactivateCustomer(String customerId) {

        return changeStatus(
                customerId,
                CustomerStatus.DEACTIVATED
        );
    }

    @Override
    @Transactional
    public CustomerResponse closeCustomer(String customerId) {

        return changeStatus(
                customerId,
                CustomerStatus.CLOSED
        );
    }

    private CustomerResponse changeStatus(
            String customerId,
            CustomerStatus targetStatus) {

        CustomerDocument customer =
                findCustomer(customerId);

        CustomerStatus previousStatus =
                customer.getStatus();

        validateTransition(
                previousStatus,
                targetStatus
        );

        customer.setStatus(targetStatus);
        customer.setUpdatedAt(Instant.now());

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        customerEventPublisher.publishCustomerStatusChanged(
                savedCustomer,
                previousStatus,
                targetStatus
        );

        return customerMapper.toResponse(savedCustomer);
    }

    private CustomerDocument findCustomer(
            String customerId) {

        return customerRepository
                .findById(customerId)
                .orElseThrow(
                        () -> new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + customerId
                        )
                );
    }

    private void validateTransition(
            CustomerStatus currentStatus,
            CustomerStatus targetStatus) {

        if (currentStatus == null) {
            throw new InvalidCustomerStateException(
                    "Customer current status cannot be null"
            );
        }

        if (currentStatus == targetStatus) {
            throw new InvalidCustomerStateException(
                    "Customer is already in status: "
                            + currentStatus
            );
        }

        boolean validTransition =
                switch (currentStatus) {

                    case PROSPECT ->
                            targetStatus ==
                                    CustomerStatus.PENDING_VERIFICATION;

                    case PENDING_VERIFICATION ->
                            targetStatus ==
                                    CustomerStatus.ACTIVE;

                    case ACTIVE ->
                            targetStatus ==
                                    CustomerStatus.SUSPENDED
                                    || targetStatus ==
                                    CustomerStatus.DEACTIVATED
                                    || targetStatus ==
                                    CustomerStatus.CLOSED;

                    case SUSPENDED ->
                            targetStatus ==
                                    CustomerStatus.ACTIVE
                                    || targetStatus ==
                                    CustomerStatus.CLOSED;

                    case DEACTIVATED ->
                            targetStatus ==
                                    CustomerStatus.ACTIVE;

                    case CLOSED ->
                            false;

                    default ->
                            false;
                };

        if (!validTransition) {
            throw new InvalidCustomerStateException(
                    "Invalid customer status transition: "
                            + currentStatus
                            + " -> "
                            + targetStatus
            );
        }
    }
}