package com.loan_org.customer_management.service;

import com.loan_org.customer_management.api.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.api.dto.response.CustomerResponse;

public interface CustomerUpdateService {
    CustomerResponse updateCustomer(String customerNumber, UpdateCustomerRequest request);
}
