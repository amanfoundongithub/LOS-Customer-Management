package com.loan_org.customer_management.service;

import com.loan_org.customer_management.api.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.api.dto.response.CustomerResponse;

public interface CustomerCreateService {
    CustomerResponse create(CreateCustomerRequest request);
}
