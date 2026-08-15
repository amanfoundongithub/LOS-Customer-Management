package com.loan_org.customer_management.customer.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {

    @Email(message = "Invalid email address")
    @Size(max = 254)
    private String email;

    @Pattern(
            regexp = "^\\+?[1-9][0-9]{7,14}$",
            message = "Invalid mobile number"
    )
    private String mobileNumber;

    @Pattern(
            regexp = "^\\+?[1-9][0-9]{7,14}$",
            message = "Invalid alternate mobile number"
    )
    private String alternateMobileNumber;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean mobileVerified = false;
}