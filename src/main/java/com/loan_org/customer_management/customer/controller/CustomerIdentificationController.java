package com.loan_org.customer_management.customer.controller;

import com.loan_org.customer_management.customer.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.customer.dto.response.IdentificationResponse;
import com.loan_org.customer_management.customer.service.CustomerIdentificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "${api.endpoint.customer.url:/api/v1/customers}"
)
@RequiredArgsConstructor
public class CustomerIdentificationController {

    private final CustomerIdentificationService
            customerIdentificationService;


    // ============================================================
    // ADD IDENTIFICATION
    // ============================================================

    @PostMapping("/{customerId}/identifications")
    public ResponseEntity<IdentificationResponse> addIdentification(

            @PathVariable String customerId,

            @Valid
            @RequestBody
            CreateIdentificationRequest request
    ) {

        IdentificationResponse response =
                customerIdentificationService.addIdentification(
                        customerId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET ALL IDENTIFICATIONS
    // ============================================================

    @GetMapping("/{customerId}/identifications")
    public ResponseEntity<List<IdentificationResponse>>
    getIdentifications(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerIdentificationService
                        .getIdentifications(customerId)
        );
    }


    // ============================================================
    // GET IDENTIFICATION
    // ============================================================

    @GetMapping(
            "/{customerId}/identifications/{identificationId}"
    )
    public ResponseEntity<IdentificationResponse>
    getIdentification(

            @PathVariable String customerId,

            @PathVariable String identificationId
    ) {

        return ResponseEntity.ok(
                customerIdentificationService
                        .getIdentification(
                                customerId,
                                identificationId
                        )
        );
    }


    // ============================================================
    // DELETE IDENTIFICATION
    // ============================================================

    @DeleteMapping(
            "/{customerId}/identifications/{identificationId}"
    )
    public ResponseEntity<Void> deleteIdentification(

            @PathVariable String customerId,

            @PathVariable String identificationId
    ) {

        customerIdentificationService.deleteIdentification(
                customerId,
                identificationId
        );

        return ResponseEntity.noContent().build();
    }
}