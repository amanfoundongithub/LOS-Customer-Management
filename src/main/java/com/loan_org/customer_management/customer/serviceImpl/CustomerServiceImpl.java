package com.loan_org.customer_management.customer.serviceImpl;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.generator.CustomerNumberGenerator;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerService;
import com.loan_org.customer_management.customer.validation.AddressValidator;
import com.loan_org.customer_management.customer.validation.CustomerValidator;
import com.loan_org.customer_management.customer.validation.IdentificationValidator;
import com.loan_org.customer_management.event.publisher.CustomerEventPublisher;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final CustomerValidator customerValidator;

    private final AddressValidator addressValidator;

    private final IdentificationValidator identificationValidator;

    private final CustomerNumberGenerator customerNumberGenerator;

    private final CustomerEventPublisher customerEventPublisher;


    // ============================================================
    // CREATE CUSTOMER
    // ============================================================

    @Override
    @Transactional
    public CustomerResponse createCustomer(
            CreateCustomerRequest request
    ) {

        /*
         * --------------------------------------------------------
         * 1. Validate request/business rules
         * --------------------------------------------------------
         */
        customerValidator.validateCreate(request);

        /*
         * --------------------------------------------------------
         * 2. Convert request DTO -> MongoDB entity
         * --------------------------------------------------------
         */
        CustomerDocument customer =
                customerMapper.toDocument(request);

        /*
         * --------------------------------------------------------
         * 3. Generate business customer number
         * --------------------------------------------------------
         */
        customer.setCustomerNumber(
                customerNumberGenerator.generate()
        );

        /*
         * --------------------------------------------------------
         * 4. Validate nested customer data
         * --------------------------------------------------------
         */
        addressValidator.validatePrimaryAddress(
                customer.getAddresses()
        );

        identificationValidator.validateDuplicateTypes(
                customer.getIdentifications()
        );

        /*
         * --------------------------------------------------------
         * 5. Persist customer
         * --------------------------------------------------------
         */
        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        /*
         * --------------------------------------------------------
         * 6. Create CUSTOMER_CREATED outbox event
         *
         * CustomerEventPublisher is now backed by
         * OutboxCustomerEventPublisher.
         *
         * Therefore this does NOT directly publish to RabbitMQ.
         *
         * It creates an outbox record in MongoDB.
         * --------------------------------------------------------
         */
        customerEventPublisher.publishCustomerCreated(
                savedCustomer
        );

        /*
         * --------------------------------------------------------
         * 7. Convert entity -> response DTO
         * --------------------------------------------------------
         */
        return customerMapper.toResponse(
                savedCustomer
        );
    }


    // ============================================================
    // GET CUSTOMER BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(
            String customerId
    ) {

        CustomerDocument customer =
                findCustomerById(customerId);

        return customerMapper.toResponse(
                customer
        );
    }


    // ============================================================
    // GET CUSTOMER BY CUSTOMER NUMBER
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCustomerNumber(
            String customerNumber
    ) {

        CustomerDocument customer =
                customerRepository
                        .findByCustomerNumber(customerNumber)
                        .orElseThrow(
                                () -> new CustomerNotFoundException(
                                        "Customer not found with customer number: "
                                                + customerNumber
                                )
                        );

        return customerMapper.toResponse(
                customer
        );
    }


    // ============================================================
    // GET CUSTOMER BY IAM USER ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByIamUserId(
            String iamUserId
    ) {

        CustomerDocument customer =
                customerRepository
                        .findByIamUserId(iamUserId)
                        .orElseThrow(
                                () -> new CustomerNotFoundException(
                                        "Customer not found for IAM user: "
                                                + iamUserId
                                )
                        );

        return customerMapper.toResponse(
                customer
        );
    }


    // ============================================================
    // UPDATE CUSTOMER
    // ============================================================

    @Override
    @Transactional
    public CustomerResponse updateCustomer(
            String customerId,
            UpdateCustomerRequest request
    ) {

        /*
         * --------------------------------------------------------
         * 1. Find existing customer
         * --------------------------------------------------------
         */
        CustomerDocument customer =
                findCustomerById(customerId);

        /*
         * --------------------------------------------------------
         * 2. Validate whether update is allowed
         * --------------------------------------------------------
         */
        customerValidator.validateUpdate(
                customer,
                request
        );

        /*
         * --------------------------------------------------------
         * 3. Apply allowed changes
         * --------------------------------------------------------
         */
        updatePersonalInformation(
                customer,
                request
        );

        /*
         * --------------------------------------------------------
         * 4. Save updated customer
         * --------------------------------------------------------
         */
        CustomerDocument savedCustomer =
                customerRepository.save(customer);

        /*
         * --------------------------------------------------------
         * 5. Publish CUSTOMER_UPDATED event through Outbox
         *
         * This creates an outbox event.
         * The background publisher will send it to RabbitMQ.
         * --------------------------------------------------------
         */
        customerEventPublisher.publishCustomerUpdated(
                savedCustomer
        );

        /*
         * --------------------------------------------------------
         * 6. Return response
         * --------------------------------------------------------
         */
        return customerMapper.toResponse(
                savedCustomer
        );
    }


    // ============================================================
    // FIND CUSTOMER
    // ============================================================

    private CustomerDocument findCustomerById(
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
    // UPDATE PERSONAL INFORMATION
    // ============================================================

    private void updatePersonalInformation(
            CustomerDocument customer,
            UpdateCustomerRequest request
    ) {

        if (request.getPersonalInformation() == null) {
            return;
        }

        customer.setPersonalInformation(
                customerMapper.toPersonalInformation(
                        request.getPersonalInformation()
                )
        );
    }
}