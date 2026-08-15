package com.loan_org.customer_management.customer.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformationResponse {

    private String email;

    private String mobileNumber;

    private String alternateMobileNumber;

    private boolean emailVerified;

    private boolean mobileVerified;
}