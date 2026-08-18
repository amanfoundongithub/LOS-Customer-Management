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
@CompoundIndex(name = "outbox_status_created_idx", def = "{'status': 1, 'createdAt': 1}")
public class OutboxEventDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String eventId;

    @Indexed
    private String aggregateId;

    @Indexed
    private String eventType;

    private int eventVersion;

    private String aggregateType;

    private String routingKey;

    private String contentType;

    private String payload;

    @Builder.Default
    @Indexed
    private OutboxEventStatus status = OutboxEventStatus.PENDING;

    private int retryCount;

    private String lastError;

    private Instant lastAttemptAt;

    private Instant publishedAt;

    @Indexed
    private Instant nextAttemptAt;

    @CreatedDate
    @Indexed
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}