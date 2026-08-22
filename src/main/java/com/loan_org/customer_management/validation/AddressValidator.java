package com.loan_org.customer_management.validation;

import java.util.List;

import com.loan_org.customer_management.entity.Address;

public interface AddressValidator {
    void validatePrimaryAddress(List<Address> addresses);
    void validateAddress(Address address);
}