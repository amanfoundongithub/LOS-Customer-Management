package com.loan_org.customer_management.customer.entity;

import com.loan_org.customer_management.customer.enums.IdentificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Identification {

    @Size(max = 50)
    private String identificationId;

    @NotNull(message = "Identification type is required")
    private IdentificationType type;

    @NotBlank(message = "Identification value is required")
    @Size(max = 200)
    private String value;

    @Size(max = 200)
    @Pattern(
            regexp = "^[A-Za-z0-9._:/-]+$",
            message = "Invalid document reference"
    )
    private String documentReference;

    @Builder.Default
    private boolean verified = false;

    private Instant verifiedAt;

    @Size(max = 200)
    private String verificationReference;
}