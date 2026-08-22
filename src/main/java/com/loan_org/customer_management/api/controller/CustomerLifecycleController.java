package com.loan_org.customer_management.api.controller;

import com.loan_org.customer_management.api.dto.response.CustomerResponse;
import com.loan_org.customer_management.service.CustomerLifecycleService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "${api.endpoint.customer.url:/api/v1/customers}"
)
@RequiredArgsConstructor
public class CustomerLifecycleController {

    private final CustomerLifecycleService
            customerLifecycleService;


    // ============================================================
    // ACTIVATE
    // ============================================================

    @PutMapping("/{customerId}/activate")
    public ResponseEntity<CustomerResponse> activateCustomer(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerLifecycleService.activateCustomer(
                        customerId
                )
        );
    }


    // ============================================================
    // SUSPEND
    // ============================================================

    @PutMapping("/{customerId}/suspend")
    public ResponseEntity<CustomerResponse> suspendCustomer(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerLifecycleService.suspendCustomer(
                        customerId
                )
        );
    }


    // ============================================================
    // DEACTIVATE
    // ============================================================

    @PutMapping("/{customerId}/deactivate")
    public ResponseEntity<CustomerResponse> deactivateCustomer(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerLifecycleService.deactivateCustomer(
                        customerId
                )
        );
    }


    // ============================================================
    // CLOSE
    // ============================================================

    @PutMapping("/{customerId}/close")
    public ResponseEntity<CustomerResponse> closeCustomer(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerLifecycleService.closeCustomer(
                        customerId
                )
        );
    }
}