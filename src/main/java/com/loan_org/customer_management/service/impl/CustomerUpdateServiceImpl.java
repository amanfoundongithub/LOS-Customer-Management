package com.loan_org.customer_management.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan_org.customer_management.api.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.api.dto.response.CustomerResponse;
import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.exception.CustomerNotFoundException;
import com.loan_org.customer_management.mapper.CustomerMapper;
import com.loan_org.customer_management.repository.CustomerRepository;
import com.loan_org.customer_management.service.CustomerUpdateService;
import com.loan_org.customer_management.validation.CustomerValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerUpdateServiceImpl implements CustomerUpdateService {

    private final CustomerRepository customerRepository;
    private final CustomerValidator  customerValidator;
    private final CustomerMapper     customerMapper;

    @Override
    
    public CustomerResponse updateCustomer(String customerNumber, UpdateCustomerRequest request) {
        log.info("Received request to update customer info for customer with cn: {}", customerNumber);
        CustomerDocument fetchedCustomer = findCustomerByCustomerNumber(customerNumber);

        log.info("Fetched customer with CN: {}", fetchedCustomer.getCustomerNumber());
        customerValidator.validateUpdate(fetchedCustomer, request);
        
        log.info("Request is valid, update in progres....");
        if(request != null) {
            fetchedCustomer.setPersonalInformation(customerMapper.toPersonalInformation(request.getPersonalInformation()));
        }
        CustomerDocument updatedDocument = customerRepository.save(fetchedCustomer);

        log.info("Dispatching event CUSTOMER_UPDATE to Kafka for CN: {}", updatedDocument.getCustomerNumber());
        return customerMapper.toResponse(updatedDocument);
    }

    private CustomerDocument findCustomerByCustomerNumber(String customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer not found with customer number: " + customerNumber)
                );
    }

}
