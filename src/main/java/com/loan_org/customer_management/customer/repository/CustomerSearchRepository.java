package com.loan_org.customer_management.customer.repository;

import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CustomerSearchRepository {
    Page<CustomerDocument> searchCustomers(
            String search,
            CustomerStatus status,
            CustomerType customerType,
            Instant createdFrom,
            Instant createdTo,
            Pageable pageable
    );
}