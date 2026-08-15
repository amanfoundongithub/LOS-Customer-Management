package com.loan_org.customer_management.customer.dto.request;

import jakarta.validation.Valid;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {

    @Valid
    private PersonalInformationRequest personalInformation;
}