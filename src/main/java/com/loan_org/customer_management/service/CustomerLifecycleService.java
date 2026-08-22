package com.loan_org.customer_management.service;

import com.loan_org.customer_management.api.dto.response.CustomerResponse;

public interface CustomerLifecycleService {

    CustomerResponse activateCustomer(
            String customerId
    );

    CustomerResponse suspendCustomer(
            String customerId
    );

    CustomerResponse deactivateCustomer(
            String customerId
    );

    CustomerResponse closeCustomer(
            String customerId
    );
}