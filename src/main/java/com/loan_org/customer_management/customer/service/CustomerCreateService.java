package com.loan_org.customer_management.customer.service;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;

public interface CustomerCreateService {
    CustomerResponse create(CreateCustomerRequest request);
}
