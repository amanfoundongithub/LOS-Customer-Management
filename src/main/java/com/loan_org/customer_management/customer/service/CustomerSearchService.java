package com.loan_org.customer_management.customer.service;

import com.loan_org.customer_management.customer.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
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