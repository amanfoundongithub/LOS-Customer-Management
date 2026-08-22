package com.loan_org.customer_management.service.impl;

import com.loan_org.customer_management.api.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.entity.CustomerDocument;
import com.loan_org.customer_management.entity.enums.CustomerStatus;
import com.loan_org.customer_management.entity.enums.CustomerType;
import com.loan_org.customer_management.mapper.CustomerMapper;
import com.loan_org.customer_management.repository.CustomerRepository;
import com.loan_org.customer_management.service.CustomerSearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerSearchServiceImpl implements CustomerSearchService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerSummaryResponse> searchCustomers(
            String search,
            CustomerStatus status,
            CustomerType customerType,
            Instant createdFrom,
            Instant createdTo,
            Pageable pageable) {

        String normalizedSearch = normalizeSearch(search);

        validateDateRange(createdFrom, createdTo);

        Page<CustomerDocument> customers =
                customerRepository.searchCustomers(
                        normalizedSearch,
                        status,
                        customerType,
                        createdFrom,
                        createdTo,
                        pageable
                );

        return customers.map(customerMapper::toSummaryResponse);
    }

    private String normalizeSearch(String search) {

        if (!StringUtils.hasText(search)) {
            return null;
        }

        return search.trim();
    }

    private void validateDateRange(
            Instant createdFrom,
            Instant createdTo) {

        if (createdFrom == null || createdTo == null) {
            return;
        }

        if (createdFrom.isAfter(createdTo)) {
            throw new IllegalArgumentException(
                    "createdFrom cannot be after createdTo"
            );
        }
    }
}