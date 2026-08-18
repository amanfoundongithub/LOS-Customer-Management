package com.loan_org.customer_management.customer.dto.request;

import com.loan_org.customer_management.customer.enums.CommunicationChannel;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferencesRequest {

    private Boolean emailNotificationsEnabled;

    private Boolean smsNotificationsEnabled;

    private Boolean marketingCommunicationEnabled;

    @Size(max = 10)
    @Pattern(
            regexp = "^[A-Za-z]{2,10}$",
            message = "Invalid preferred language"
    )
    private String preferredLanguage;

    private CommunicationChannel preferredCommunicationChannel;
}