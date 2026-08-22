package com.loan_org.customer_management.service;

import com.loan_org.customer_management.api.dto.response.CustomerResponse;

public interface CustomerFetchService {
    CustomerResponse getCustomerById(String customerId);
    CustomerResponse getCustomerByCustomerNumber(String customerNumber);
    CustomerResponse getCustomerByIamUserId(String iamUserId);
}
