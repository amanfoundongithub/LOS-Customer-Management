package com.loan_org.customer_management.customer.controller;

import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.customer.url:/api/v1/customers}")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // ============================================================
    // CREATE CUSTOMER
    // ============================================================

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid
            @RequestBody
            CreateCustomerRequest request
    ) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET CUSTOMER BY ID
    // ============================================================

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable String customerId
    ) {

        CustomerResponse response =
                customerService.getCustomerById(customerId);

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // GET CUSTOMER BY CUSTOMER NUMBER
    // ============================================================

    @GetMapping("/number/{customerNumber}")
    public ResponseEntity<CustomerResponse> getCustomerByCustomerNumber(
            @PathVariable String customerNumber
    ) {

        CustomerResponse response =
                customerService.getCustomerByCustomerNumber(
                        customerNumber
                );

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // GET CUSTOMER BY IAM USER ID
    // ============================================================

    @GetMapping("/iam/{iamUserId}")
    public ResponseEntity<CustomerResponse> getCustomerByIamUserId(
            @PathVariable String iamUserId
    ) {

        CustomerResponse response =
                customerService.getCustomerByIamUserId(
                        iamUserId
                );

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // UPDATE CUSTOMER
    // ============================================================

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable String customerId,

            @Valid
            @RequestBody
            UpdateCustomerRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomer(
                        customerId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}