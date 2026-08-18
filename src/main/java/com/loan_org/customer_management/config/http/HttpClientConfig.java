package com.loan_org.customer_management.config.http;

import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientConfig {

    private final HttpClientProperties clientProperties;

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofMillis(clientProperties.getReadTimeoutMs()));
        return builder.requestFactory(requestFactory).build();
    }

}
