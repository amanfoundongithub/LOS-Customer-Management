package com.loan_org.customer_management.generator;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;

public interface CustomerNumberGenerator {
    String generate(CreateCustomerRequest request);
}
