package com.loan_org.customer_management.event.event.data;

import com.loan_org.customer_management.entity.enums.CustomerStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatusChangedEvent {
    private String customerId;
    private String customerNumber;
    private CustomerStatus previousStatus;
    private CustomerStatus newStatus;
}