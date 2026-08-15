package com.loan_org.customer_management.customer.dto.response;

import com.loan_org.customer_management.customer.enums.IdentificationType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentificationResponse {

    private String identificationId;

    private IdentificationType type;

    /**
     * Prefer returning a masked value.
     */
    private String value;

    private String documentReference;

    private boolean verified;

    private Instant verifiedAt;

    private String verificationReference;
}