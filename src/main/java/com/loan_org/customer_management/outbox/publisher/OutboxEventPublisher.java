package com.loan_org.customer_management.outbox.publisher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan_org.customer_management.configuration.properties.MdcProperties;
import com.loan_org.customer_management.configuration.properties.OutboxProperties;
import com.loan_org.customer_management.configuration.properties.RabbitMQProperties;
import com.loan_org.customer_management.outbox.entity.OutboxEventDocument;
import com.loan_org.customer_management.outbox.entity.OutboxEventStatus;
import com.loan_org.customer_management.outbox.repository.OutboxEventRepository;
import com.loan_org.customer_management.outbox.service.OutboxService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
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
    private final MdcProperties mdcProperties;
    private final RabbitMQProperties rabbitMQProperties;
    private final OutboxProperties outboxProperties;

    @Scheduled(
            fixedDelayString = "#{@outboxProperties.pollIntervalMs}"
    )
    public void publishPendingEvents() {

        List<OutboxEventDocument> events =
                outboxEventRepository.findEligibleEvents(
                        OutboxEventStatus.PENDING,
                        Instant.now(),
                        PageRequest.of(
                                0,
                                outboxProperties.getBatchSize()
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

        event.setLastAttemptAt(Instant.now());
        event.setRetryCount(
                event.getRetryCount() + 1
        );

        outboxEventRepository.save(event);

        try {

            JsonNode payload =
                    objectMapper.readTree(
                            event.getPayload()
                    );

            setMdcFromPayload(payload);

            rabbitTemplate.convertAndSend(
                    rabbitMQProperties
                            .getExchange()
                            .getName(),
                    event.getRoutingKey(),
                    payload
            );

            outboxService.markPublished(event);

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

            outboxService.markFailed(
                    event,
                    exception
            );

        } finally {

            MDC.remove(
                    mdcProperties
                            .getCorrelation()
                            .getMdcKey()
            );

            MDC.remove(
                    mdcProperties
                            .getTrace()
                            .getMdcKey()
            );
        }
    }

    private void setMdcFromPayload(
            JsonNode payload
    ) {

        String correlationKey =
                mdcProperties
                        .getCorrelation()
                        .getMdcKey();

        String traceKey =
                mdcProperties
                        .getTrace()
                        .getMdcKey();

        if (payload.hasNonNull(correlationKey)) {
            MDC.put(
                    correlationKey,
                    payload.get(correlationKey).asText()
            );
        }

        if (payload.hasNonNull(traceKey)) {
            MDC.put(
                    traceKey,
                    payload.get(traceKey).asText()
            );
        }
    }
}