package com.loan_org.customer_management.customer.service.impl;

import org.springframework.stereotype.Service;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerCreateService;
import com.loan_org.customer_management.customer.validation.AddressValidator;
import com.loan_org.customer_management.customer.validation.CustomerValidator;
import com.loan_org.customer_management.customer.validation.IdentificationValidator;
import com.loan_org.customer_management.event.publisher.CustomerEventPublisher;
import com.loan_org.customer_management.generator.CustomerNumberGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerCreateServiceImpl implements CustomerCreateService {

    private final CustomerValidator       customerValidator;
    private final AddressValidator        addressValidator;
    private final IdentificationValidator identificationValidator;
    private final CustomerNumberGenerator customerNumberGenerator;
    private final CustomerMapper          customerMapper;
    private final CustomerRepository      customerRepository;
    private final CustomerEventPublisher  customerEventPublisher;

    @Override
    
    public CustomerResponse create(CreateCustomerRequest request) {

        log.info("Received request to CREATE_CUSTOMER for IAM userId: {}. Starting process...", request.getIamUserId());
        customerValidator.validateCreate(request);

        log.info("Completed customer request validation. Now completing insertion...", request);
        CustomerDocument customer = customerMapper.toDocument(request);
        String newCustomerNumber  = customerNumberGenerator.generate(request);
        customer.setCustomerNumber(newCustomerNumber);
        log.info("Generated a new customer number: {} for IAM ID: {}", newCustomerNumber, request.getIamUserId());

        addressValidator.validatePrimaryAddress(customer.getAddresses());
        identificationValidator.validateDuplicateTypes(customer.getIdentifications());
        log.info("Address has been verified, as well as identity for CN: {}", newCustomerNumber);

        CustomerDocument savedCustomer = customerRepository.save(customer);
        customerEventPublisher.publishCustomerCreated(savedCustomer);
        log.info("Successfully done and published CUSTOMER_CREATE for ECN: {} with MongoID: {}", newCustomerNumber, customer.getId());

        return customerMapper.toResponse(savedCustomer);
    }
    
}
