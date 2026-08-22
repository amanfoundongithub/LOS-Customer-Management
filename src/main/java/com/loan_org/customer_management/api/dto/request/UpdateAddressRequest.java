package com.loan_org.customer_management.api.dto.request;

import com.loan_org.customer_management.entity.enums.AddressType;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequest {

    private AddressType type;

    @Size(max = 200)
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

    private Boolean primary;
}