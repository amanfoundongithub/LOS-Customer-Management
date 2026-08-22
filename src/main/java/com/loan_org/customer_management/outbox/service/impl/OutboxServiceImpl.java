package com.loan_org.customer_management.outbox.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan_org.customer_management.configuration.properties.RabbitMQProperties;
import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;
import com.loan_org.customer_management.outbox.entity.OutboxEventStatus;
import com.loan_org.customer_management.outbox.repository.OutboxEventRepository;
import com.loan_org.customer_management.outbox.service.OutboxService;
import com.loan_org.customer_management.utils.ErrorTruncator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService {

    private static final int CURRENT_EVENT_VERSION = 1;
    private static final String CONTENT_TYPE = "application/json";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final RabbitMQProperties rabbitMQProperties;

    @Override
    public OutboxEventDocument createEvent(
            String eventType,
            String aggregateType,
            String aggregateId,
            String routingKey,
            Object payload
    ) {
        String serializedPayload;
        try {
            serializedPayload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            log.error(
                    "Failed to serialize outbox event | eventType={} aggregateId={}",
                    eventType,
                    aggregateId,
                    exception
            );
            throw new IllegalStateException(
                    "Unable to serialize outbox event",
                    exception
            );
        }
        OutboxEventDocument event = OutboxEventDocument.builder()
                        .eventId(UUID.randomUUID().toString())
                        .eventType(eventType)
                        .eventVersion(CURRENT_EVENT_VERSION)
                        .aggregateType(aggregateType)
                        .aggregateId(aggregateId)
                        .routingKey(routingKey)
                        .contentType(CONTENT_TYPE)
                        .payload(serializedPayload)
                        .status(OutboxEventStatus.PENDING)
                        .retryCount(0)
                        .build();
        return outboxEventRepository.save(event);
    }

    @Override
    public void markPublished(OutboxEventDocument event) {
        event.setStatus(OutboxEventStatus.PUBLISHED);
        event.setPublishedAt(Instant.now());
        event.setLastError(null);
        event.setNextAttemptAt(null);
        outboxEventRepository.save(event);
        log.info(
                "Outbox event marked as published | eventId={} eventType={} aggregateId={}",
                event.getEventId(),
                event.getEventType(),
                event.getAggregateId()
        );
    }

    @Override
    public void markFailed(OutboxEventDocument event, Exception exception) {
        RabbitMQProperties.Retry retry = rabbitMQProperties.getRetry();
        int retryCount = event.getRetryCount() + 1;
        Instant now = Instant.now();

        event.setRetryCount(retryCount);
        event.setLastAttemptAt(now);
        event.setLastError(ErrorTruncator.truncateError(exception.getMessage()));

        if (shouldRetry(retry, retryCount)) {
            Instant nextAttemptAt = calculateNextAttemptAt(now, retry, retryCount);
            event.setStatus(OutboxEventStatus.PENDING);
            event.setNextAttemptAt(nextAttemptAt);
            outboxEventRepository.save(event);
            log.warn(
                    "Outbox event scheduled for retry | eventId={} eventType={} retryCount={} nextAttemptAt={}",
                    event.getEventId(),
                    event.getEventType(),
                    retryCount,
                    nextAttemptAt
            );
            return;
        }

        event.setStatus(OutboxEventStatus.FAILED);
        event.setNextAttemptAt(null);
        outboxEventRepository.save(event);
        log.error(
                "Outbox event permanently failed | eventId={} eventType={} retryCount={} maxAttempts={}",
                event.getEventId(),
                event.getEventType(),
                retryCount,
                retry.getMaxAttempts(),
                exception
        );
    }

    private boolean shouldRetry(RabbitMQProperties.Retry retry, int retryCount) {
        if (!retry.isEnabled()) {
            return false;
        }
        return retryCount < retry.getMaxAttempts();
    }

    private Instant calculateNextAttemptAt(Instant now, RabbitMQProperties.Retry retry, int retryCount) {
        double delay =
                retry.getInitialIntervalMs()
                        * Math.pow(
                                retry.getMultiplier(),
                                retryCount - 1
                        );

        long delayMs =
                Math.min(
                        (long) delay,
                        retry.getMaxIntervalMs()
                );

        return now.plusMillis(delayMs);
    }

}