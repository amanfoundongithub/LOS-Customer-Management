package com.loan_org.customer_management.validation;

import com.loan_org.customer_management.api.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.api.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.entity.CustomerDocument;

public interface CustomerValidator {
    void validateCreate(CreateCustomerRequest request);
    void validateUpdate(CustomerDocument customer, UpdateCustomerRequest request);
}