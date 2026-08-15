package com.loan_org.customer_management.customer.service;

import com.loan_org.customer_management.customer.dto.request.UpdatePreferencesRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerPreferencesResponse;

public interface CustomerPreferencesService {

    CustomerPreferencesResponse getPreferences(
            String customerId
    );

    CustomerPreferencesResponse updatePreferences(
            String customerId,
            UpdatePreferencesRequest request
    );
}