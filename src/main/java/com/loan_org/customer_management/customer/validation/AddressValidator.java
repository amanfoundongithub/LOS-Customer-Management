package com.loan_org.customer_management.customer.validation;

import com.loan_org.customer_management.customer.entity.Address;

import java.util.List;

public interface AddressValidator {
    void validatePrimaryAddress(List<Address> addresses);
    void validateAddress(Address address);
}