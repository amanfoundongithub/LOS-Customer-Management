package com.loan_org.customer_management.api.dto.request;

import com.loan_org.customer_management.entity.enums.IdentificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdentificationRequest {

    @NotNull
    private IdentificationType type;

    @NotBlank
    @Size(max = 200)
    private String value;

    @Size(max = 200)
    private String documentReference;
}