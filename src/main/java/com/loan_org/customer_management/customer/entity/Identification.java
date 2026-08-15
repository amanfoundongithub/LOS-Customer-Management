package com.loan_org.customer_management.customer.entity;

import com.loan_org.customer_management.customer.enums.IdentificationType;
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

    /**
     * Internal identification record identifier.
     */
    @Size(max = 50)
    private String identificationId;

    private IdentificationType type;

    /**
     * Prefer storing a masked/tokenized value
     * rather than sensitive identification data in
     * plain text.
     */
    @Size(max = 200)
    private String value;

    /**
     * Reference to the Document Management service.
     */
    @Size(max = 200)
    @Pattern(
            regexp = "^[A-Za-z0-9._:/-]+$",
            message = "Invalid document reference"
    )
    private String documentReference;

    @Builder.Default
    private boolean verified = false;

    private Instant verifiedAt;

    /**
     * Reference returned by Verification service.
     */
    @Size(max = 200)
    private String verificationReference;
}