package com.loan_org.customer_management.config.outbox;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OutboxProperties.class)
public class OutboxConfig {
    
    @Bean
    public OutboxProperties outboxProperties(OutboxProperties properties) {
        return properties;
    }
}
