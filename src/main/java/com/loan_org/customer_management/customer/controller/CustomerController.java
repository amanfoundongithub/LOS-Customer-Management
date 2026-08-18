package com.loan_org.customer_management.customer.controller;

import com.loan_org.customer_management.customer.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.customer.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateAddressRequest;
import com.loan_org.customer_management.customer.dto.request.UpdateCustomerRequest;
import com.loan_org.customer_management.customer.dto.response.AddressResponse;
import com.loan_org.customer_management.customer.dto.response.CustomerResponse;
import com.loan_org.customer_management.customer.service.AddressService;
import com.loan_org.customer_management.customer.service.CustomerCreateService;
import com.loan_org.customer_management.customer.service.CustomerFetchService;
import com.loan_org.customer_management.customer.service.CustomerUpdateService;

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

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerCreateService.create(request)
        );
    }

    @GetMapping("/id/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                customerFetchService.getCustomerById(customerId)
        );
    }

    @GetMapping("/number/{customerNumber}")
    public ResponseEntity<CustomerResponse> getCustomerByCustomerNumber(@PathVariable String customerNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(
                customerFetchService.getCustomerByCustomerNumber(customerNumber)
        );
    }

    @GetMapping("/iam/{iamUserId}")
    public ResponseEntity<CustomerResponse> getCustomerByIamUserId(@PathVariable String iamUserId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                customerFetchService.getCustomerByIamUserId(iamUserId)
        );
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable String customerId, @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
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
        return ResponseEntity.status(HttpStatus.OK).body(
            addressService.getAddresses(customerId)
        );
    }

    @GetMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(@PathVariable String customerId, @PathVariable String addressId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            addressService.getAddress(customerId, addressId)
        );
    }

    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable String customerId, @PathVariable String addressId, @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
            addressService.updateAddress(customerId, addressId, request)
        );
    }

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String customerId, @PathVariable String addressId) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{customerId}/addresses/{addressId}/primary")
    public ResponseEntity<AddressResponse> setPrimaryAddress(@PathVariable String customerId, @PathVariable String addressId) {
        addressService.setPrimaryAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }

}