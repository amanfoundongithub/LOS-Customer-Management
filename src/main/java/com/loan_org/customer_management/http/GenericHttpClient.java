package com.loan_org.customer_management.http;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

public interface GenericHttpClient {
    <T> T get(String url, Class<T> responseType);
    <T> T get(String url, Class<T> responseType, Map<String, String> headers);
    <T> T get(
        String url,
        ParameterizedTypeReference<T> responseType
);

<T> T get(
            String url,
            ParameterizedTypeReference<T> responseType,
            Map<String, String> headers
    );

    <R, T> T post(String url, R request, Class<T> responseType);
    <R, T> T post(String url, Map<String, String> headers, R request, Class<T> responseType);

    <R, T> T put(String url, R request, Class<T> responseType);
    <R, T> T put(String url, Map<String, String> headers, R request, Class<T> responseType);

    void delete(String url);
    void delete(String url, Map<String, String> headers);
}
