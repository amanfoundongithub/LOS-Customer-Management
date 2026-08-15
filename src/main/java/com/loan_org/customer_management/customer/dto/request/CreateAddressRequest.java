package com.loan_org.customer_management.customer.dto.request;

import com.loan_org.customer_management.customer.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {

    @NotNull
    private AddressType type;

    @NotBlank
    @Size(max = 200)
    private String addressLine1;

    @Size(max = 200)
    private String addressLine2;

    @Size(max = 100)
    private String landmark;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String district;

    @NotBlank
    @Size(max = 100)
    private String state;

    @NotBlank
    @Pattern(
            regexp = "^[A-Za-z0-9 -]{3,12}$",
            message = "Invalid postal code"
    )
    private String postalCode;

    @NotBlank
    @Size(max = 100)
    private String country;

    private Boolean primary;
}