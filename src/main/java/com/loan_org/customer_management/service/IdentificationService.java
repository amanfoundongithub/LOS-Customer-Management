package com.loan_org.customer_management.service;

import java.util.List;

import com.loan_org.customer_management.api.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.api.dto.response.IdentificationResponse;

public interface IdentificationService {

    IdentificationResponse addIdentification(
            String customerId,
            CreateIdentificationRequest request
    );

    List<IdentificationResponse> getIdentifications(
            String customerId
    );

    IdentificationResponse getIdentification(
            String customerId,
            String identificationId
    );

    void deleteIdentification(
            String customerId,
            String identificationId
    );
}