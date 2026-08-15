package com.loan_org.customer_management.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContactRequest {

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
}