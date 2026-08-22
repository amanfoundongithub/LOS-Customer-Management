package com.loan_org.customer_management.event.event.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerIdentificationChangedEvent {
    private String customerId;
    private String customerNumber;
    private String identificationId;
    private String changeType;
}