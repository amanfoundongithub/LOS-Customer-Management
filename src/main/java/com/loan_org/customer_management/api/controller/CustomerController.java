package com.loan_org.customer_management.api.controller;

import com.loan_org.customer_management.api.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.api.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.api.dto.request.CreateIdentificationRequest;
import com.loan_org.customer_management.api.dto.request.UpdateAddressRequest;
import com.loan_org.customer_management.api.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.api.dto.request.UpdatePreferencesRequest;
import com.loan_org.customer_management.api.dto.response.AddressResponse;
import com.loan_org.customer_management.api.dto.response.CustomerPreferencesResponse;
import com.loan_org.customer_management.api.dto.response.CustomerResponse;
import com.loan_org.customer_management.api.dto.response.IdentificationResponse;
import com.loan_org.customer_management.service.AddressService;
import com.loan_org.customer_management.service.CustomerCreateService;
import com.loan_org.customer_management.service.CustomerFetchService;
import com.loan_org.customer_management.service.CustomerUpdateService;
import com.loan_org.customer_management.service.IdentificationService;
import com.loan_org.customer_management.service.PreferencesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.customer.basePath}")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerCreateService customerCreateService;
    private final CustomerFetchService  customerFetchService;
    private final CustomerUpdateService customerUpdateService;
    private final AddressService        addressService;
    private final IdentificationService identificationService;
    private final PreferencesService    preferencesService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerCreateService.create(request)
        );
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String customerId) {
        return ResponseEntity.ok(
                customerFetchService.getCustomerById(customerId)
        );
    }

    @GetMapping("/number/{customerNumber}")
    public ResponseEntity<CustomerResponse> getCustomerByCustomerNumber(@PathVariable String customerNumber) {
        return ResponseEntity.ok(
                customerFetchService.getCustomerByCustomerNumber(customerNumber)
        );
    }

    @GetMapping("/iam/{iamUserId}")
    public ResponseEntity<CustomerResponse> getCustomerByIamUserId(@PathVariable String iamUserId) {
        return ResponseEntity.ok(
                customerFetchService.getCustomerByIamUserId(iamUserId)
        );
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable String customerId, @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(
                customerUpdateService.updateCustomer(customerId, request)
        );
    }

    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<AddressResponse> addAddress(@PathVariable String customerId, @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            addressService.addAddress(customerId, request)
        );
    }

    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<AddressResponse>> getAddresses(@PathVariable String customerId) {
        return ResponseEntity.ok(
            addressService.getAddresses(customerId)
        );
    }

    @GetMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(@PathVariable String customerId, @PathVariable String addressId) {
        return ResponseEntity.ok(
            addressService.getAddress(customerId, addressId)
        );
    }

    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable String customerId, @PathVariable String addressId, @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(
            addressService.updateAddress(customerId, addressId, request)
        );
    }

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String customerId, @PathVariable String addressId) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{customerId}/addresses/{addressId}/primary")
    public ResponseEntity<Void> setPrimaryAddress(@PathVariable String customerId, @PathVariable String addressId) {
        addressService.setPrimaryAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{customerId}/identifications")
    public ResponseEntity<IdentificationResponse> addIdentification(@PathVariable String customerId, @Valid @RequestBody CreateIdentificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            identificationService.addIdentification(customerId, request)
        );
    }

    @GetMapping("/{customerId}/identifications")
    public ResponseEntity<List<IdentificationResponse>> getIdentifications(@PathVariable String customerId) {
        return ResponseEntity.ok(
            identificationService.getIdentifications(customerId)
        );
    }

    @GetMapping("/{customerId}/identifications/{identificationId}")
    public ResponseEntity<IdentificationResponse> getIdentification(@PathVariable String customerId, @PathVariable String identificationId) {
        return ResponseEntity.ok(
            identificationService.getIdentification(customerId, identificationId)
        );
    }

    @DeleteMapping("/{customerId}/identifications/{identificationId}")
    public ResponseEntity<Void> deleteIdentification(@PathVariable String customerId, @PathVariable String identificationId) {
        identificationService.deleteIdentification(customerId, identificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}/preferences")
    public ResponseEntity<CustomerPreferencesResponse> getPreferences(@PathVariable String customerId) {
        return ResponseEntity.ok(
            preferencesService.getPreferences(customerId)
        );
    }

    @PutMapping("/{customerId}/preferences")
    public ResponseEntity<CustomerPreferencesResponse> updatePreferences(@PathVariable String customerId, @Valid @RequestBody UpdatePreferencesRequest request) {
        return ResponseEntity.ok(
            preferencesService.updatePreferences(customerId, request)
        );
    }

}