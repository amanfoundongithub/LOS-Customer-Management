package com.loan_org.customer_management.config.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private String version = "v1";
    private Customer customer = new Customer();

    @Getter
    @Setter
    public static class Customer {
        private String basePath = "/api/v1/customers";
    }
}
