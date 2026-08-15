package com.loan_org.customer_management.outbox.service;

import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;

public interface OutboxService {

    OutboxEventDocument createEvent(
            String eventType,
            String aggregateType,
            String aggregateId,
            String routingKey,
            Object payload
    );

    void markPublished(
            OutboxEventDocument event
    );

    void markFailed(
            OutboxEventDocument event,
            Exception exception
    );
}