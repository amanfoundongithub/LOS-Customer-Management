package com.loan_org.customer_management.customer.entity;

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

    /**
     * Example:
     * en
     * hi
     * te
     */
    @Size(max = 10)
    @Pattern(
            regexp = "^[A-Za-z]{2,10}$",
            message = "Invalid preferred language"
    )
    private String preferredLanguage;

    /**
     * Example:
     * EMAIL
     * SMS
     * PHONE
     */
    @Size(max = 30)
    private String preferredCommunicationChannel;
}