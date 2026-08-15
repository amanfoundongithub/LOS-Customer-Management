package com.loan_org.customer_management.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;
import com.loan_org.customer_management.outbox.entity.OutboxEventStatus;
import com.loan_org.customer_management.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl
        implements OutboxService {

    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper;


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

            serializedPayload =
                    objectMapper.writeValueAsString(payload);

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

        OutboxEventDocument event =
                OutboxEventDocument.builder()
                        .eventId(UUID.randomUUID().toString())
                        .eventType(eventType)
                        .aggregateType(aggregateType)
                        .aggregateId(aggregateId)
                        .routingKey(routingKey)
                        .payload(serializedPayload)
                        .status(OutboxEventStatus.PENDING)
                        .retryCount(0)
                        .build();

        return outboxEventRepository.save(event);
    }


    @Override
    public void markPublished(
            OutboxEventDocument event
    ) {

        event.setStatus(
                OutboxEventStatus.PUBLISHED
        );

        event.setPublishedAt(
                Instant.now()
        );

        event.setLastError(null);

        outboxEventRepository.save(event);
    }


    @Override
    public void markFailed(
            OutboxEventDocument event,
            Exception exception
    ) {

        event.setStatus(
                OutboxEventStatus.FAILED
        );

        event.setRetryCount(
                event.getRetryCount() + 1
        );

        event.setLastAttemptAt(
                Instant.now()
        );

        event.setLastError(
                truncateError(
                        exception.getMessage()
                )
        );

        outboxEventRepository.save(event);
    }


    private String truncateError(
            String error
    ) {

        if (error == null) {
            return null;
        }

        /*
         * Don't allow an enormous RabbitMQ/database
         * exception to make the outbox document huge.
         */
        int maxLength = 2000;

        if (error.length() <= maxLength) {
            return error;
        }

        return error.substring(
                0,
                maxLength
        );
    }
}