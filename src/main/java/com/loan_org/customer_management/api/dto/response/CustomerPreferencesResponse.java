package com.loan_org.customer_management.api.dto.response;

import com.loan_org.customer_management.entity.enums.CommunicationChannel;

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

    private CommunicationChannel preferredCommunicationChannel;
}