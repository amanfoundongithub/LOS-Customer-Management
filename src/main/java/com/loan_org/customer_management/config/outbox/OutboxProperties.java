package com.loan_org.customer_management.config.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "outbox")
public class OutboxProperties {
    private int  batchSize;
    private long pollIntervalMs;
    private int  maxRetries;
}