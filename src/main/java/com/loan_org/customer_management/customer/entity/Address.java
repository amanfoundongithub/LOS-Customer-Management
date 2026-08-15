package com.loan_org.customer_management.customer.entity;

import com.loan_org.customer_management.customer.enums.AddressType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    /**
     * Internal address identifier.
     *
     * Example:
     * ADDR-000001
     */
    @Size(max = 50)
    private String addressId;

    private AddressType type;

    @Size(min = 1, max = 200)
    private String addressLine1;

    @Size(max = 200)
    private String addressLine2;

    @Size(max = 100)
    private String landmark;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    private String state;

    @Pattern(
            regexp = "^[A-Za-z0-9 -]{3,12}$",
            message = "Invalid postal code"
    )
    private String postalCode;

    @Size(max = 100)
    private String country;

    @Builder.Default
    private boolean primary = false;
}