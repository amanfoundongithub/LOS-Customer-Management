package com.loan_org.customer_management.utils.generator;

import com.loan_org.customer_management.api.dto.request.CreateCustomerRequest;

public interface CustomerNumberGenerator {
    String generate(CreateCustomerRequest request);
}
