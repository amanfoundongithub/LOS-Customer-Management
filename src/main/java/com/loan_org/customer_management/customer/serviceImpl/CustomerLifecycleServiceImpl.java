package com.loan_org.customer_management.customer.serviceImpl;

import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerLifecycleService;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.exception.InvalidCustomerStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerLifecycleServiceImpl
        implements CustomerLifecycleService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;



    // ============================================================
    // ACTIVATE CUSTOMER
    // ============================================================

    @Override
    public CustomerResponse activateCustomer(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateTransition(
                customer.getStatus(),
                CustomerStatus.ACTIVE
        );

        customer.setStatus(
                CustomerStatus.ACTIVE
        );

        updateLifecycleTimestamp(customer);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }


    // ============================================================
    // SUSPEND CUSTOMER
    // ============================================================

    @Override
    public CustomerResponse suspendCustomer(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateTransition(
                customer.getStatus(),
                CustomerStatus.SUSPENDED
        );

        customer.setStatus(
                CustomerStatus.SUSPENDED
        );

        updateLifecycleTimestamp(customer);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }


    // ============================================================
    // DEACTIVATE CUSTOMER
    // ============================================================

    @Override
    public CustomerResponse deactivateCustomer(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateTransition(
                customer.getStatus(),
                CustomerStatus.DEACTIVATED
        );

        customer.setStatus(
                CustomerStatus.DEACTIVATED
        );

        updateLifecycleTimestamp(customer);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }


    // ============================================================
    // CLOSE CUSTOMER
    // ============================================================

    @Override
    public CustomerResponse closeCustomer(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomer(customerId);

        validateTransition(
                customer.getStatus(),
                CustomerStatus.CLOSED
        );

        customer.setStatus(
                CustomerStatus.CLOSED
        );

        updateLifecycleTimestamp(customer);

        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }


    // ============================================================
    // FIND CUSTOMER
    // ============================================================

    private CustomerDocument findCustomer(
            String customerId
    ) {

        return customerRepository
                .findById(customerId)
                .orElseThrow(
                        () -> new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + customerId
                        )
                );
    }


    // ============================================================
    // VALIDATE STATE TRANSITION
    // ============================================================

    private void validateTransition(
            CustomerStatus currentStatus,
            CustomerStatus targetStatus
    ) {

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
                                    ||
                                    targetStatus ==
                                            CustomerStatus.DEACTIVATED
                                    ||
                                    targetStatus ==
                                            CustomerStatus.CLOSED;

                    case SUSPENDED ->
                            targetStatus ==
                                    CustomerStatus.ACTIVE
                                    ||
                                    targetStatus ==
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


    // ============================================================
    // LIFECYCLE TIMESTAMP
    // ============================================================

    private void updateLifecycleTimestamp(
            CustomerDocument customer
    ) {

        customer.setUpdatedAt(
                Instant.now()
        );
    }
}