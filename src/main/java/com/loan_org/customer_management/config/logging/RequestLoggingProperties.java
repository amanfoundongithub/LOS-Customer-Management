package com.loan_org.customer_management.config.logging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "requestLogging")
public class RequestLoggingProperties {
    private boolean enabled = true;
    private boolean includeQueryString = true;
    private boolean includeCorrelationId = true;
    private boolean includeTraceId = true;
    private boolean includeDuration = true;
}