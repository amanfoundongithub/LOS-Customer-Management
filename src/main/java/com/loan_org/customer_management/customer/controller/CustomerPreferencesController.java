package com.loan_org.customer_management.customer.controller;

import com.loan_org.customer_management.customer.dto.request.UpdatePreferencesRequest;
import com.loan_org.customer_management.customer.dto.response.CustomerPreferencesResponse;
import com.loan_org.customer_management.customer.service.CustomerPreferencesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "${api.endpoint.customer.url:/api/v1/customers}"
)
@RequiredArgsConstructor
public class CustomerPreferencesController {

    private final CustomerPreferencesService
            customerPreferencesService;


    // ============================================================
    // GET PREFERENCES
    // ============================================================

    @GetMapping("/{customerId}/preferences")
    public ResponseEntity<CustomerPreferencesResponse>
    getPreferences(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerPreferencesService.getPreferences(
                        customerId
                )
        );
    }


    // ============================================================
    // UPDATE PREFERENCES
    // ============================================================

    @PutMapping("/{customerId}/preferences")
    public ResponseEntity<CustomerPreferencesResponse>
    updatePreferences(

            @PathVariable String customerId,

            @Valid
            @RequestBody
            UpdatePreferencesRequest request
    ) {

        return ResponseEntity.ok(
                customerPreferencesService.updatePreferences(
                        customerId,
                        request
                )
        );
    }
}