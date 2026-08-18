package com.loan_org.customer_management.config.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "http")
public class HttpClientProperties {

    private boolean enabled;
    private int     connectTimeoutMs;
    private int     readTimeoutMs;
    private Retry   retry = new Retry();
    private Headers headers = new Headers();

    @Getter
    @Setter
    public static class Retry {
        private boolean enabled;
        private int     maxAttempts;
        private long    initialDelayMs;
        private double  multiplier;
        private long    maxDelayMs;
    }

    @Getter
    @Setter
    public static class Headers {
        private boolean propagateCorrelationId;
        private boolean propagateTraceId;
        private String  correlationHeader;
        private String  traceHeader;
        private String  userAgent;
    }
}