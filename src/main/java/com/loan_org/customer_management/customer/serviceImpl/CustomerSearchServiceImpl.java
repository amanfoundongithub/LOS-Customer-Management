package com.loan_org.customer_management.customer.serviceImpl;

import com.loan_org.customer_management.customer.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.customer.entity.CustomerDocument;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import com.loan_org.customer_management.customer.mapper.CustomerMapper;
import com.loan_org.customer_management.customer.repository.CustomerRepository;
import com.loan_org.customer_management.customer.service.CustomerSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerSearchServiceImpl
        implements CustomerSearchService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;


    // ============================================================
    // SEARCH CUSTOMERS
    // ============================================================

    @Override
    public Page<CustomerSummaryResponse> searchCustomers(
            String search,
            CustomerStatus status,
            CustomerType customerType,
            Instant createdFrom,
            Instant createdTo,
            Pageable pageable
    ) {

        /*
         * --------------------------------------------------------
         * 1. Normalize search input
         * --------------------------------------------------------
         */
        String normalizedSearch =
                normalizeSearch(search);


        /*
         * --------------------------------------------------------
         * 2. Validate date range
         * --------------------------------------------------------
         */
        validateDateRange(
                createdFrom,
                createdTo
        );


        /*
         * --------------------------------------------------------
         * 3. Query MongoDB
         * --------------------------------------------------------
         */
        Page<CustomerDocument> customers =
                customerRepository.searchCustomers(
                        normalizedSearch,
                        status,
                        customerType,
                        createdFrom,
                        createdTo,
                        pageable
                );


        /*
         * --------------------------------------------------------
         * 4. Convert entities -> summary responses
         * --------------------------------------------------------
         */
        return customers.map(
                customerMapper::toSummaryResponse
        );
    }


    // ============================================================
    // NORMALIZE SEARCH
    // ============================================================

    private String normalizeSearch(
            String search
    ) {

        if (!StringUtils.hasText(search)) {
            return null;
        }

        return search.trim();
    }


    // ============================================================
    // DATE VALIDATION
    // ============================================================

    private void validateDateRange(
            Instant createdFrom,
            Instant createdTo
    ) {

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