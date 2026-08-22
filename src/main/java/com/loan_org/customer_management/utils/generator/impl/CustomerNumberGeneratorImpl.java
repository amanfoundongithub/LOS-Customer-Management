package com.loan_org.customer_management.utils.generator.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.loan_org.customer_management.api.dto.request.CreateCustomerRequest;
import com.loan_org.customer_management.utils.generator.CustomerNumberGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerNumberGeneratorImpl implements CustomerNumberGenerator {
    
    private static final String CUSTOMER_PREFIX = "CUST-";
    
    @Override
    public String generate(CreateCustomerRequest request) {
        String suffix = UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0,8)
                            .toUpperCase();
        return CUSTOMER_PREFIX + suffix;
    }   

}
