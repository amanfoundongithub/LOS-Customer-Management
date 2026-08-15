package com.loan_org.customer_management.customer.controller;

import com.loan_org.customer_management.customer.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateAddressRequest;
import com.loan_org.customer_management.customer.dto.response.AddressResponse;
import com.loan_org.customer_management.customer.service.CustomerAddressService;
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
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;


    // ============================================================
    // ADD ADDRESS
    // ============================================================

    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<AddressResponse> addAddress(

            @PathVariable String customerId,

            @Valid
            @RequestBody
            CreateAddressRequest request
    ) {

        AddressResponse response =
                customerAddressService.addAddress(
                        customerId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET ALL ADDRESSES
    // ============================================================

    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<AddressResponse>> getAddresses(

            @PathVariable String customerId
    ) {

        return ResponseEntity.ok(
                customerAddressService.getAddresses(
                        customerId
                )
        );
    }


    // ============================================================
    // GET ADDRESS
    // ============================================================

    @GetMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(

            @PathVariable String customerId,

            @PathVariable String addressId
    ) {

        return ResponseEntity.ok(
                customerAddressService.getAddress(
                        customerId,
                        addressId
                )
        );
    }


    // ============================================================
    // UPDATE ADDRESS
    // ============================================================

    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(

            @PathVariable String customerId,

            @PathVariable String addressId,

            @Valid
            @RequestBody
            UpdateAddressRequest request
    ) {

        return ResponseEntity.ok(
                customerAddressService.updateAddress(
                        customerId,
                        addressId,
                        request
                )
        );
    }


    // ============================================================
    // DELETE ADDRESS
    // ============================================================

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(

            @PathVariable String customerId,

            @PathVariable String addressId
    ) {

        customerAddressService.deleteAddress(
                customerId,
                addressId
        );

        return ResponseEntity.noContent().build();
    }


    // ============================================================
    // SET PRIMARY ADDRESS
    // ============================================================

    @PutMapping("/{customerId}/addresses/{addressId}/primary")
    public ResponseEntity<AddressResponse> setPrimaryAddress(

            @PathVariable String customerId,

            @PathVariable String addressId
    ) {
        customerAddressService.setPrimaryAddress(
                        customerId,
                        addressId
                );
        return ResponseEntity.noContent(
                
        ).build();
    }
}