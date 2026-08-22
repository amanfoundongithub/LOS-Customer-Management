package com.loan_org.customer_management.configuration.config;


import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.loan_org.customer_management.configuration.properties.HttpClientProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientConfig {

    private final HttpClientProperties clientProperties;

    @Bean
    RestClient restClient() {
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                                                .setConnectTimeout(Timeout.ofMilliseconds(clientProperties.getConnectTimeoutMs()))
                                                .build();
        RequestConfig requestConfig       = RequestConfig.custom()
                                                .setResponseTimeout(Timeout.ofMilliseconds(clientProperties.getReadTimeoutMs()))
                                                .build();
        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setDefaultConnectionConfig(connectionConfig)
                        .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                                                .setConnectionManager(connectionManager)
                                                .setDefaultRequestConfig(requestConfig)
                                                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        
        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}