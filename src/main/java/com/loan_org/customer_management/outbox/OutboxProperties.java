package com.loan_org.customer_management.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "outbox")
public class OutboxProperties {
    private int batchSize = 50;
    private long pollIntervalMs = 2000;
    private int maxRetries = 10;
}