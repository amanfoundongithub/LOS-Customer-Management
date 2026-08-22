package com.loan_org.customer_management.service;

import java.util.List;

import com.loan_org.customer_management.api.dto.request.CreateAddressRequest;
import com.loan_org.customer_management.api.dto.request.UpdateAddressRequest;
import com.loan_org.customer_management.api.dto.response.AddressResponse;

public interface AddressService {
    AddressResponse       addAddress(String customerId, CreateAddressRequest request);
    List<AddressResponse> getAddresses(String customerId);
    AddressResponse       getAddress(String customerId, String addressId);
    AddressResponse       updateAddress(String customerId, String addressId, UpdateAddressRequest request);
    void                  deleteAddress(String customerId, String addressId);
    void                  setPrimaryAddress(String customerId, String addressId);
}