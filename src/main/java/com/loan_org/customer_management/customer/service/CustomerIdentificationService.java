package com.loan_org.customer_management.customer.service;

import com.loan_org.customer_management.customer.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.customer.dto.response.IdentificationResponse;

import java.util.List;

public interface CustomerIdentificationService {

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