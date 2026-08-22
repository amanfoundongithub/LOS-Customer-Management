package com.loan_org.customer_management.service;

import com.loan_org.customer_management.api.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.entity.enums.CustomerStatus;
import com.loan_org.customer_management.entity.enums.CustomerType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CustomerSearchService {

    Page<CustomerSummaryResponse> searchCustomers(
            String search,
            CustomerStatus status,
            CustomerType customerType,
            Instant createdFrom,
            Instant createdTo,
            Pageable pageable
    );
}