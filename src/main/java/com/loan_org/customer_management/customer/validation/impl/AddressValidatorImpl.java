package com.loan_org.customer_management.customer.validation.impl;

import com.loan_org.customer_management.customer.entity.Address;
import com.loan_org.customer_management.customer.validation.AddressValidator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressValidatorImpl implements AddressValidator {

    public void validatePrimaryAddress(
            List<Address> addresses
    ) {

        if (addresses == null || addresses.isEmpty()) {
            return;
        }

        long primaryAddressCount =
                addresses.stream()
                        .filter(Address::isPrimary)
                        .count();

        if (primaryAddressCount > 1) {

            throw new IllegalStateException(
                    "Customer cannot have more than one primary address"
            );
        }
    }

    public void validateAddress(
            Address address
    ) {

        if (address == null) {
            throw new IllegalArgumentException(
                    "Address cannot be null"
            );
        }

        if (address.getType() == null) {
            throw new IllegalArgumentException(
                    "Address type is required"
            );
        }
    }
}