package com.loan_org.customer_management.http.impl;

import com.loan_org.customer_management.config.http.HttpClientProperties;
import com.loan_org.customer_management.http.GenericHttpClient;
import com.loan_org.customer_management.http.exception.HttpClientException;
import com.loan_org.customer_management.http.exception.HttpRetryException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericHttpClientImpl implements GenericHttpClient {

    private final RestClient restClient;
    private final HttpClientProperties properties;

    @Override
    public <T> T get(String url, Class<T> responseType) {
        return exchange(HttpMethod.GET, url, null, null, responseType);
    }

    @Override
    public <T> T get(String url, Class<T> responseType, Map<String, String> headers) {
        return exchange(HttpMethod.GET, url, null, toHttpHeaders(headers), responseType);
    }

    @Override
    public <R, T> T post(String url, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.POST, url, requestBody, null, responseType);
    }

    @Override
    public <R, T> T post(String url, Map<String, String> headers, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.POST, url, requestBody, toHttpHeaders(headers), responseType);
    }

    @Override
    public <R, T> T put(String url, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.PUT, url, requestBody, null, responseType);
    }

    @Override
    public <R, T> T put(String url, Map<String, String> headers, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.PUT, url, requestBody, toHttpHeaders(headers), responseType);
    }

    @Override
    public void delete(String url) {
        exchange(HttpMethod.DELETE, url, null, null, null);
    }

    @Override
    public void delete(String url, Map<String, String> headers) {
        exchange(HttpMethod.DELETE, url, null, toHttpHeaders(headers), null);
    }

    public <T, R> T exchange(HttpMethod method, String url, R requestBody, HttpHeaders headers, Class<T> responseType) {
        if (!properties.isEnabled()) {
            throw new HttpClientException("HTTP client is disabled", null);
        }

        int maxAttempts = properties.getRetry().isEnabled() ? properties.getRetry().getMaxAttempts() : 1;
        long delay = properties.getRetry().getInitialDelayMs();
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return execute(method, url, requestBody, headers, responseType);
            } catch (HttpClientException exception) {
                lastException = exception;

                if (!isRetryable(exception.getStatusCode())) {
                    throw exception;
                }

                if (attempt >= maxAttempts) {
                    break;
                }

                log.warn("HTTP request failed, retrying | method={} url={} attempt={}/{} status={}", method, url, attempt, maxAttempts, exception.getStatusCode());
                sleep(delay);
                delay = calculateNextDelay(delay);
            }
        }

        throw new HttpRetryException("HTTP request failed after " + maxAttempts + " attempts: " + method + " " + url, lastException);
    }

    private <T, R> T execute(HttpMethod method, String url, R requestBody, HttpHeaders headers, Class<T> responseType) {
        try {
            RestClient.RequestBodySpec request = restClient
                    .method(method)
                    .uri(url)
                    .headers(httpHeaders -> applyHeaders(httpHeaders, headers));

            if (requestBody != null) {
                request.contentType(MediaType.APPLICATION_JSON).body(requestBody);
            }

            return request
                    .retrieve()
                    .onStatus(status -> status.isError(), (request1, response) -> {
                        throw new HttpClientException(
                                "HTTP request failed: " + method + " " + url,
                                response.getStatusCode().value(),
                                response.getBody().toString());
                    })
                    .body(responseType);

        } catch (HttpClientException exception) {
            throw exception;
        } catch (RestClientException exception) {
            throw new HttpClientException("HTTP request execution failed: " + method + " " + url, exception);
        }
    }

    private void applyHeaders(HttpHeaders target, HttpHeaders additional) {
        target.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        if (properties.getHeaders().getUserAgent() != null) {
            target.set(HttpHeaders.USER_AGENT, properties.getHeaders().getUserAgent());
        }

        String correlationId = MDC.get(properties.getHeaders().getCorrelationHeader());
        String traceId = MDC.get(properties.getHeaders().getTraceHeader());

        if (properties.getHeaders().isPropagateCorrelationId() && correlationId != null) {
            target.set(properties.getHeaders().getCorrelationHeader(), correlationId);
        }

        if (properties.getHeaders().isPropagateTraceId() && traceId != null) {
            target.set(properties.getHeaders().getTraceHeader(), traceId);
        }

        if (additional != null) {
            target.putAll(additional);
        }
    }

    private HttpHeaders toHttpHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        return httpHeaders;
    }

    private boolean isRetryable(int statusCode) {
        return statusCode == 408 || statusCode == 429 || statusCode >= 500;
    }

    private long calculateNextDelay(long currentDelay) {
        double multiplier = properties.getRetry().getMultiplier();
        long maxDelay = properties.getRetry().getMaxDelayMs();
        return Math.min((long) (currentDelay * multiplier), maxDelay);
    }

    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new HttpRetryException("HTTP retry interrupted", exception);
        }
    }
}