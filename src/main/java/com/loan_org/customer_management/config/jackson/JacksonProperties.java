package com.loan_org.customer_management.config.jackson;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jackson")
public class JacksonProperties {

    private Serialization   serialization   = new Serialization();
    private Deserialization deserialization = new Deserialization();
    private Inclusion       inclusion       = new Inclusion();
    private Naming          naming          = new Naming();

    @Getter
    @Setter
    public static class Serialization {
        private boolean writeDatesAsTimestamps = false;
        private boolean writeDatesWithZoneId = false;
        private boolean prettyPrint = false;
        private boolean failOnEmptyBeans = true;
    }

    @Getter
    @Setter
    public static class Deserialization {
        private boolean failOnUnknownProperties = true;
        private boolean acceptEmptyStringAsNull = false;
        private boolean acceptSingleValueAsArray = false;
        private boolean readUnknownEnumValuesAsNull = false;
    }

    @Getter
    @Setter
    public static class Inclusion {
        private String value = "NON_NULL";
        private String nullValueHandling = "SET";
        private String content = "ALWAYS";
    }

    @Getter
    @Setter
    public static class Naming {
        private String strategy = "LOWER_CAMEL_CASE";
    }

}
