package com.loan_org.customer_management.service;

import com.loan_org.customer_management.api.dto.request.UpdatePreferencesRequest;
import com.loan_org.customer_management.api.dto.response.CustomerPreferencesResponse;

public interface PreferencesService {

    CustomerPreferencesResponse getPreferences(
            String customerId
    );

    CustomerPreferencesResponse updatePreferences(
            String customerId,
            UpdatePreferencesRequest request
    );
}