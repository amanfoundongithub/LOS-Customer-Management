package com.loan_org.customer_management.customer.dto.request;

import com.loan_org.customer_management.customer.enums.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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