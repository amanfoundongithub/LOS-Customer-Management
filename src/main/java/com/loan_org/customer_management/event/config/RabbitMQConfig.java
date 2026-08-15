package com.loan_org.customer_management.event.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /*
     * ============================================================
     * EXCHANGE
     * ============================================================
     */

    public static final String CUSTOMER_EXCHANGE =
            "los.customer.exchange";


    /*
     * ============================================================
     * ROUTING KEYS
     * ============================================================
     */

    public static final String CUSTOMER_CREATED =
            "customer.created";

    public static final String CUSTOMER_UPDATED =
            "customer.updated";

    public static final String CUSTOMER_STATUS_CHANGED =
            "customer.status.changed";

    public static final String CUSTOMER_ADDRESS_CHANGED =
            "customer.address.changed";

    public static final String CUSTOMER_IDENTIFICATION_CHANGED =
            "customer.identification.changed";


    /*
     * ============================================================
     * QUEUES
     * ============================================================
     *
     * These are example queues for consumers.
     *
     * The Customer service publishes events.
     * Other services consume them.
     */

    public static final String NOTIFICATION_QUEUE =
            "los.customer.notification";

    public static final String AUDIT_QUEUE =
            "los.customer.audit";


    /*
     * ============================================================
     * EXCHANGE
     * ============================================================
     */

    @Bean
    public DirectExchange customerExchange() {

        return new DirectExchange(
                CUSTOMER_EXCHANGE,
                true,
                false
        );
    }


    /*
     * ============================================================
     * JSON MESSAGE CONVERTER
     * ============================================================
     */

    @Bean
    public MessageConverter messageConverter() {

        return new JacksonJsonMessageConverter();
    }


    /*
     * ============================================================
     * NOTIFICATION QUEUE
     * ============================================================
     */

    @Bean
    public Queue customerNotificationQueue() {

        return new Queue(
                NOTIFICATION_QUEUE,
                true
        );
    }


    /*
     * ============================================================
     * AUDIT QUEUE
     * ============================================================
     */

    @Bean
    public Queue customerAuditQueue() {

        return new Queue(
                AUDIT_QUEUE,
                true
        );
    }


    /*
     * ============================================================
     * NOTIFICATION BINDINGS
     * ============================================================
     */

    @Bean
    public Binding customerCreatedNotificationBinding(
            Queue customerNotificationQueue,
            DirectExchange customerExchange
    ) {

        return BindingBuilder
                .bind(customerNotificationQueue)
                .to(customerExchange)
                .with(CUSTOMER_CREATED);
    }


    @Bean
    public Binding customerUpdatedNotificationBinding(
            Queue customerNotificationQueue,
            DirectExchange customerExchange
    ) {

        return BindingBuilder
                .bind(customerNotificationQueue)
                .to(customerExchange)
                .with(CUSTOMER_UPDATED);
    }


    /*
     * ============================================================
     * AUDIT BINDINGS
     * ============================================================
     */

    @Bean
    public Binding customerCreatedAuditBinding(
            Queue customerAuditQueue,
            DirectExchange customerExchange
    ) {

        return BindingBuilder
                .bind(customerAuditQueue)
                .to(customerExchange)
                .with(CUSTOMER_CREATED);
    }


    @Bean
    public Binding customerUpdatedAuditBinding(
            Queue customerAuditQueue,
            DirectExchange customerExchange
    ) {

        return BindingBuilder
                .bind(customerAuditQueue)
                .to(customerExchange)
                .with(CUSTOMER_UPDATED);
    }


    @Bean
    public Binding customerStatusChangedAuditBinding(
            Queue customerAuditQueue,
            DirectExchange customerExchange
    ) {

        return BindingBuilder
                .bind(customerAuditQueue)
                .to(customerExchange)
                .with(CUSTOMER_STATUS_CHANGED);
    }
}