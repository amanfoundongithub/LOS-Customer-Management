package com.loan_org.customer_management.outbox.publisher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;
import com.loan_org.customer_management.outbox.entity.OutboxEventStatus;
import com.loan_org.customer_management.outbox.repository.OutboxEventRepository;
import com.loan_org.customer_management.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {

    private final OutboxEventRepository outboxEventRepository;

    private final OutboxService outboxService;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.outbox.batch-size:50}")
    private int batchSize;


    /**
     * Poll MongoDB for pending outbox events.
     *
     * Runs every 2 seconds by default.
     */
    @Scheduled(
            fixedDelayString =
                    "${rabbitmq.outbox.poll-interval-ms:2000}"
    )
    public void publishPendingEvents() {

        List<OutboxEventDocument> events =
                outboxEventRepository
                        .findByStatusOrderByCreatedAtAsc(
                                OutboxEventStatus.PENDING,
                                PageRequest.of(
                                        0,
                                        batchSize,
                                        Sort.by(
                                                Sort.Direction.ASC,
                                                "createdAt"
                                        )
                                )
                        );

        if (events.isEmpty()) {
            return;
        }

        log.debug(
                "Found {} pending outbox events",
                events.size()
        );

        for (OutboxEventDocument event : events) {

            publishEvent(event);
        }
    }


    private void publishEvent(
            OutboxEventDocument event
    ) {

        event.setLastAttemptAt(
                Instant.now()
        );

        event.setRetryCount(
                event.getRetryCount() + 1
        );

        outboxEventRepository.save(event);

        try {

            JsonNode payload =
                    objectMapper.readTree(
                            event.getPayload()
                    );

            String correlationId =
                    payload.has("correlationId")
                            ? payload.get("correlationId").asText()
                            : null;

            String traceId =
                    payload.has("traceId")
                            ? payload.get("traceId").asText()
                            : null;

            if (correlationId != null) {
                MDC.put(
                        "correlationId",
                        correlationId
                );
            }

            if (traceId != null) {
                MDC.put(
                        "traceId",
                        traceId
                );
            }

            rabbitTemplate.convertAndSend(
                    "los.customer.exchange",
                    event.getRoutingKey(),
                    payload
            );

            outboxService.markPublished(
                    event
            );

            log.info(
                    "Outbox event published | eventId={} eventType={} aggregateId={} routingKey={}",
                    event.getEventId(),
                    event.getEventType(),
                    event.getAggregateId(),
                    event.getRoutingKey()
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to publish outbox event | eventId={} eventType={} aggregateId={} retryCount={}",
                    event.getEventId(),
                    event.getEventType(),
                    event.getAggregateId(),
                    event.getRetryCount(),
                    exception
            );

            /*
             * Put it back into PENDING so the next poll
             * will retry it.
             */
            event.setStatus(
                    OutboxEventStatus.PENDING
            );

            outboxService.markFailed(
                    event,
                    exception
            );

            /*
             * markFailed sets FAILED.
             *
             * We deliberately change it back to PENDING
             * here because transient RabbitMQ failures
             * should automatically retry.
             */
            event.setStatus(
                    OutboxEventStatus.PENDING
            );

            outboxEventRepository.save(event);

        } finally {

            MDC.remove("correlationId");
            MDC.remove("traceId");
        }
    }
}