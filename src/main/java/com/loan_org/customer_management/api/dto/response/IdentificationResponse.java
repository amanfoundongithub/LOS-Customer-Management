package com.loan_org.customer_management.api.dto.response;

import lombok.*;

import java.time.Instant;

import com.loan_org.customer_management.entity.enums.IdentificationType;

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