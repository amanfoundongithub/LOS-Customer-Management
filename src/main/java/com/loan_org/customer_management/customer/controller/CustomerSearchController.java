package com.loan_org.customer_management.customer.controller;

import com.loan_org.customer_management.customer.dto.response.CustomerSummaryResponse;
import com.loan_org.customer_management.customer.enums.CustomerStatus;
import com.loan_org.customer_management.customer.enums.CustomerType;
import com.loan_org.customer_management.customer.service.CustomerSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerSearchController {

    private final CustomerSearchService customerSearchService;

    @GetMapping
    public ResponseEntity<Page<CustomerSummaryResponse>> searchCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) CustomerStatus status,
            @RequestParam(required = false) CustomerType customerType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant createdFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant createdTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        Sort.Direction direction =
                Sort.Direction.fromOptionalString(sortDirection)
                        .orElse(Sort.Direction.DESC);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        Page<CustomerSummaryResponse> response =
                customerSearchService.searchCustomers(
                        search,
                        status,
                        customerType,
                        createdFrom,
                        createdTo,
                        pageable
                );

        return ResponseEntity.ok(response);
    }
}