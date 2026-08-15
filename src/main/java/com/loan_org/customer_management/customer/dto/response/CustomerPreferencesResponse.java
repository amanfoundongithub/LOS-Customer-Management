package com.loan_org.customer_management.customer.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPreferencesResponse {

    private boolean emailNotificationsEnabled;

    private boolean smsNotificationsEnabled;

    private boolean marketingCommunicationEnabled;

    private String preferredLanguage;

    private String preferredCommunicationChannel;
}