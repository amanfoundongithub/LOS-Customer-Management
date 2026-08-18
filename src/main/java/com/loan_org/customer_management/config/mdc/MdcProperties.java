package com.loan_org.customer_management.config.mdc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mdc")
public class MdcProperties {
    private boolean enabled;
    private Correlation correlation = new Correlation();
    private Trace trace = new Trace();
    private Response response = new Response();
    private Validation validation = new Validation();

    @Getter
    @Setter
    public static class Correlation {
        private String header;
        private String mdcKey;
        private boolean acceptIncoming;
        private boolean generateIfMissing;
    }

    @Getter
    @Setter
    public static class Trace {
        private String header;
        private String mdcKey;
        private boolean acceptIncoming;
        private boolean generateIfMissing;
    }

    @Getter
    @Setter
    public static class Response {
        private boolean includeCorrelationId;
        private boolean includeTraceId;
    }

    @Getter
    @Setter
    public static class Validation {
        private int maxIdLength;
    }
}
