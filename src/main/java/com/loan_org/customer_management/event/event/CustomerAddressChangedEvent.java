package com.loan_org.customer_management.event.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressChangedEvent {

    private String customerId;

    private String customerNumber;

    private String addressId;

    private String changeType;
}