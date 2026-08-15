package com.loan_org.customer_management.customer.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerNumberGenerator {

    public String generate() {

        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "CUST-" + suffix;
    }
}