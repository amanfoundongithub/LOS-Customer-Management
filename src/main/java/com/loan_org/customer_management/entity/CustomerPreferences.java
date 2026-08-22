package com.loan_org.customer_management.entity;

import com.loan_org.customer_management.entity.enums.CommunicationChannel;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPreferences {

    @Builder.Default
    private boolean emailNotificationsEnabled = true;

    @Builder.Default
    private boolean smsNotificationsEnabled = true;

    @Builder.Default
    private boolean marketingCommunicationEnabled = false;

    @Size(max = 10)
    @Pattern(
            regexp = "^[A-Za-z]{2,3}(-[A-Za-z]{2,4})?$",
            message = "Invalid preferred language"
    )
    private String preferredLanguage;

    private CommunicationChannel preferredCommunicationChannel;
}