package com.loan_org.customer_management.config.mdc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "config.mdc")
public class MdcProperties {
    private boolean enabled = true;
    private Correlation correlation = new Correlation();
    private Trace trace = new Trace();
    private Response response = new Response();
    private Validation validation = new Validation();

    @Getter
    @Setter
    public static class Correlation {
        private String header = "X-Correlation-Id";
        private String mdcKey = "correlationId";
        private boolean acceptIncoming = true;
        private boolean generateIfMissing = true;
    }

    @Getter
    @Setter
    public static class Trace {
        private String header = "X-Trace-Id";
        private String mdcKey = "traceId";
        private boolean acceptIncoming = true;
        private boolean generateIfMissing = true;
    }

    @Getter
    @Setter
    public static class Response {
        private boolean includeCorrelationId = true;
        private boolean includeTraceId = true;
    }

    @Getter
    @Setter
    public static class Validation {
        private int maxIdLength = 128;
    }
}
