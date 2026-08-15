package com.loan_org.customer_management.outbox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "outbox_events")
@CompoundIndex(
        name = "outbox_status_created_idx",
        def = "{'status': 1, 'createdAt': 1}"
)
public class OutboxEventDocument {

    @Id
    private String id;

    /**
     * Globally unique identifier for this event.
     */
    @Indexed(unique = true)
    private String eventId;

    /**
     * Example:
     *
     * CUSTOMER_CREATED
     * CUSTOMER_UPDATED
     * CUSTOMER_STATUS_CHANGED
     */
    @Indexed
    private String eventType;

    /**
     * Example:
     *
     * CUSTOMER
     * CUSTOMER_ADDRESS
     * CUSTOMER_IDENTIFICATION
     */
    private String aggregateType;

    /**
     * ID of the entity that caused this event.
     */
    @Indexed
    private String aggregateId;

    /**
     * RabbitMQ routing key.
     *
     * Example:
     * customer.created
     */
    private String routingKey;

    /**
     * Serialized JSON event payload.
     */
    private String payload;

    /**
     * Current publishing state.
     */
    @Indexed
    private OutboxEventStatus status;

    /**
     * Number of times publishing was attempted.
     */
    private int retryCount;

    /**
     * Last error returned while publishing.
     */
    private String lastError;

    /**
     * Time of the last publish attempt.
     */
    private Instant lastAttemptAt;

    /**
     * Time at which RabbitMQ successfully accepted
     * the event.
     */
    private Instant publishedAt;

    @CreatedDate
    @Indexed
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}