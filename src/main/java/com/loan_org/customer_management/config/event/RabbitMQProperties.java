package com.loan_org.customer_management.config.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "event.rabbitmq")
public class RabbitMQProperties {
    private boolean enabled = true;
    private Exchange exchange = new Exchange();
    private Queue customerEvents = new Queue();
    private Publisher publisher = new Publisher();
    private Consumer consumer = new Consumer();
    private Retry retry = new Retry();

    @Getter
    @Setter
    public static class Exchange {
        private String name = "customer.events";
        private String type = "topic";
        private boolean durable = true;
        private boolean autoDelete = false;
    }

    @Getter
    @Setter
    public static class Queue {
        private String name = "customer.events.queue";
        private boolean durable = true;
        private boolean exclusive = false;
        private boolean autoDelete = false;
        private String routingKey = "customer.*";
    }

    @Getter
    @Setter
    public static class Publisher {
        private boolean enabled = true;
        private boolean confirms = true;
        private boolean returns = true;
    }

    @Getter
    @Setter
    public static class Consumer {
        private boolean enabled = true;
        private int concurrency = 1;
        private int maxConcurrency = 5;
        private int prefetch = 10;
        private String acknowledgementMode = "manual";
    }

    @Getter
    @Setter
    public static class Retry {
        private boolean enabled = true;
        private int maxAttempts = 3;
        private long initialIntervalMs = 1000;
        private double multiplier = 2.0;
        private long maxIntervalMs = 10000;
        private boolean deadLetterEnabled = true;
        private String deadLetterExchange = "customer.events.dlx";
        private String deadLetterQueue = "customer.events.dlq";
    }

}
