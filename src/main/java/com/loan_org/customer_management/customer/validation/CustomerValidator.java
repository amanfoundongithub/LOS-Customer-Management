package com.loan_org.customer_management.customer.validation;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.entity.CustomerDocument;

public interface CustomerValidator {
    void validateCreate(CreateCustomerRequest request);
    void validateUpdate(CustomerDocument customer, UpdateCustomerRequest request);
}