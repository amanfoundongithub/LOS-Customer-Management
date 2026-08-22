package com.loan_org.customer_management.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

import com.loan_org.customer_management.entity.enums.CustomerType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    private String iamUserId;

    @Valid
    @NotNull
    private PersonalInformationRequest personalInformation;

    @Valid
    @NotNull
    private ContactInformationRequest contactInformation;

    @Valid
    private List<CreateAddressRequest> addresses;

    @Valid
    private List<CreateIdentificationRequest> identifications;

    @NotNull
    private CustomerType customerType;
}