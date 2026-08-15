package com.loan_org.customer_management.event.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEvent<T> {

    private String eventId;

    private String eventType;

    private Instant occurredAt;

    private String customerId;

    private String customerNumber;

    private String correlationId;

    private String traceId;

    private T data;
}