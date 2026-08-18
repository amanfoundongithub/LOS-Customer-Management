package com.loan_org.customer_management.customer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerFetchService;
import com.loan_org.customer_management.exception.CustomerNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerFetchServiceImpl implements CustomerFetchService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper     customerMapper;

    @Override
    public CustomerResponse getCustomerById(String customerId) {
        return customerMapper.toResponse(findCustomerById(customerId));
    }

    @Override
    public CustomerResponse getCustomerByCustomerNumber(String customerNumber) {
        return customerMapper.toResponse(findCustomerByCustomerNumber(customerNumber));
    }

    @Override
    
    public CustomerResponse getCustomerByIamUserId(String iamUserId) {
        return customerMapper.toResponse(findByIamUserId(iamUserId));
    }

    private CustomerDocument findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer not found with id: " + customerId)
                );
    }

    private CustomerDocument findCustomerByCustomerNumber(String customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer not found with customer number: " + customerNumber)
                );
    }

    private CustomerDocument findByIamUserId(String iamUserid) {
        return customerRepository.findByIamUserId(iamUserid)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer not found with iam user id: " + iamUserid)
                );
    }

}
