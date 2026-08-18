package com.loan_org.customer_management.config.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.http-client")
public class HttpClientProperties {

    private boolean enabled = true;
    private int connectTimeoutMs = 5000;
    private int readTimeoutMs = 10000;
    private Retry retry = new Retry();
    private Headers headers = new Headers();

    @Getter
    @Setter
    public static class Retry {

        private boolean enabled = true;

        private int maxAttempts = 3;

        private long initialDelayMs = 500;

        private double multiplier = 2.0;

        private long maxDelayMs = 5000;
    }

    @Getter
    @Setter
    public static class Headers {

        private boolean propagateCorrelationId = true;

        private boolean propagateTraceId = true;

        private String correlationHeader = "X-Correlation-Id";

        private String traceHeader = "X-Trace-Id";

        private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    }
}