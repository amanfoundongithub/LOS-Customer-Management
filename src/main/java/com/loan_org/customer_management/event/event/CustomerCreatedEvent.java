package com.loan_org.customer_management.event.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreatedEvent {

    private String customerId;

    private String customerNumber;

    private String iamUserId;

    private String email;

    private String mobileNumber;
}