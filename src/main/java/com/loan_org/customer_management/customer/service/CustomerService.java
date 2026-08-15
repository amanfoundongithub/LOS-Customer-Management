package com.loan_org.customer_management.customer.service;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse createCustomer(
            CreateCustomerRequest request
    );

    CustomerResponse getCustomerById(
            String customerId
    );

    CustomerResponse getCustomerByCustomerNumber(
            String customerNumber
    );

    CustomerResponse getCustomerByIamUserId(
            String iamUserId
    );

    CustomerResponse updateCustomer(
            String customerId,
            UpdateCustomerRequest request
    );
}