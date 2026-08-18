package com.loan_org.customer_management.customer.service;

import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;

public interface CustomerUpdateService {
    CustomerResponse updateCustomer(String customerNumber, UpdateCustomerRequest request);
}
