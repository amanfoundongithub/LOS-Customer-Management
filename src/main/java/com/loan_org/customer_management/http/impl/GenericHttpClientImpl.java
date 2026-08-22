package com.loan_org.customer_management.http.impl;

import com.loan_org.customer_management.http.GenericHttpClient;
import com.loan_org.customer_management.http.exception.HttpClientException;
import com.loan_org.customer_management.http.exception.HttpRetryException;
import com.loan_org.customer_management.properties.HttpClientProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericHttpClientImpl implements GenericHttpClient {

    private final RestClient           restClient;
    private final HttpClientProperties properties;

    @Override
    public <T> T get(String url, Class<T> responseType) {
        return exchange(HttpMethod.GET, url,null,null, responseType);
    }

    @Override
    public <T> T get(String url, Class<T> responseType, Map<String, String> headers) {
        return exchange(HttpMethod.GET, url,null, toHttpHeaders(headers), responseType);
    }

    @Override
    public <T> T get(String url, ParameterizedTypeReference<T> responseType) {
        return exchange(HttpMethod.GET, url,null,null, responseType);
    }

    @Override
    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Map<String, String> headers) {
        return exchange(HttpMethod.GET, url,null, toHttpHeaders(headers), responseType);
    }

    @Override
    public <R, T> T post(String url, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.POST, url, requestBody,null, responseType);
    }

    @Override
    public <R, T> T post(String url, Map<String, String> headers, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.POST, url, requestBody, toHttpHeaders(headers), responseType);
    }

    @Override
    public <R, T> T put(String url, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.PUT, url, requestBody,null, responseType);
    }

    @Override
    public <R, T> T put(String url, Map<String, String> headers, R requestBody, Class<T> responseType) {
        return exchange(HttpMethod.PUT, url, requestBody, toHttpHeaders(headers), responseType);
    }

    @Override
    public void delete(String url) {
        exchange(HttpMethod.DELETE, url,null, null,Void.class);
    }

    @Override
    public void delete(String url, Map<String, String> headers) {
        exchange(HttpMethod.DELETE, url,null, toHttpHeaders(headers),Void.class);
    }

    public <R, T> T exchange(HttpMethod method,String url,R requestBody,HttpHeaders headers,Class<T> responseType) {
        if (!properties.isEnabled()) {
            throw new HttpClientException("HTTP client is disabled. Please enable to start the client.",null);
        }

        // Retry configurations 
        int maxAttempts = properties.getRetry().isEnabled() ? Math.max(1, properties.getRetry().getMaxAttempts()): 1;
        long delay      = properties.getRetry().getInitialDelayMs();

        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                log.debug("Executing HTTP request | method={} url={} attempt={}/{}", method, url, attempt, maxAttempts);
                return execute(method,url,requestBody,headers,responseType);
            } catch (HttpClientException exception) {
                lastException = exception;
                if (!isRetryable(exception.getStatusCode())) {
                    log.error("Non-retryable HTTP request failure | method={} url={} status={}",method,url,exception.getStatusCode());
                    throw exception;
                }
                if (attempt >= maxAttempts) {
                    break;
                }
                log.warn("Retryable HTTP request failure | method={} url={} attempt={}/{} status={} retryDelayMs={}",
                        method,
                        url,
                        attempt,
                        maxAttempts,
                        exception.getStatusCode(),
                        delay
                );
                sleep(delay);
                delay = calculateNextDelay(delay);
            } catch (RestClientException exception) {
                lastException = exception;
                if (attempt >= maxAttempts) {
                    break;
                }
                log.warn(
                        "Transient HTTP transport failure, retrying | method={} url={} attempt={}/{} retryDelayMs={}",
                        method,
                        url,
                        attempt,
                        maxAttempts,
                        delay,
                        exception
                );
                sleep(delay);
                delay = calculateNextDelay(delay);
            }
        }
        throw new HttpRetryException("HTTP request failed after " + maxAttempts + " attempts: " + method + " " + url, lastException);
    }

    public <R, T> T exchange(HttpMethod method,String url,R requestBody,HttpHeaders headers,ParameterizedTypeReference<T> responseType) {
        if (!properties.isEnabled()) {
            throw new HttpClientException("HTTP client is disabled. Please enable to start the client.",null);
        }
        int maxAttempts = properties.getRetry().isEnabled() ? Math.max(1, properties.getRetry().getMaxAttempts()) : 1;
        long delay      = properties.getRetry().getInitialDelayMs();

        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                log.debug(
                        "Executing HTTP request | method={} url={} attempt={}/{}",
                        method,
                        url,
                        attempt,
                        maxAttempts
                );
                return execute(method,url,requestBody,headers,responseType);
            } catch (HttpClientException exception) {
                lastException = exception;
                if (!isRetryable(exception.getStatusCode())) {
                    log.error(
                            "Non-retryable HTTP request failure | method={} url={} status={}",
                            method,
                            url,
                            exception.getStatusCode()
                    );
                    throw exception;
                }
                if (attempt >= maxAttempts) {
                    break;
                }
                log.warn(
                        "Retryable HTTP request failure | method={} url={} attempt={}/{} status={} retryDelayMs={}",
                        method,
                        url,
                        attempt,
                        maxAttempts,
                        exception.getStatusCode(),
                        delay
                );
                sleep(delay);
                delay = calculateNextDelay(delay);
            } catch (RestClientException exception) {
                lastException = exception;
                if (attempt >= maxAttempts) {
                    break;
                }
                log.warn(
                        "Transient HTTP transport failure, retrying | method={} url={} attempt={}/{} retryDelayMs={}",
                        method,
                        url,
                        attempt,
                        maxAttempts,
                        delay,
                        exception
                );
                sleep(delay);
                delay = calculateNextDelay(delay);
            }
        }
        throw new HttpRetryException("HTTP request failed after " + maxAttempts + " attempts: " + method + " " + url, lastException);
    }

    private <R, T> T execute(HttpMethod method,String url,R requestBody,HttpHeaders headers,Class<T> responseType) {
        try {
            RestClient.RequestBodySpec request = createRequest(method,url,requestBody,headers);
            if (responseType == null || responseType == Void.class) {
                request.retrieve().toBodilessEntity();
                return null;
            }
            return request.retrieve()
                .onStatus(
                        status -> status.isError(),
                        (request1, response) -> handleErrorResponse(method,url,response))
                .body(responseType);

        } catch (HttpClientException exception) {
            throw exception;

        } catch (RestClientException exception) {
            throw exception;
        }
    }

    private <R, T> T execute(HttpMethod method,String url,R requestBody,HttpHeaders headers,ParameterizedTypeReference<T> responseType) {
        try {
            RestClient.RequestBodySpec request = createRequest(method,url,requestBody,headers);
            if (responseType == null) {
                request.retrieve().toBodilessEntity();
                return null;
            }
            return request.retrieve()
                .onStatus(
                        status -> status.isError(),
                        (request1, response) -> handleErrorResponse(method,url,response))
                .body(responseType);
        } catch (HttpClientException exception) {
            throw exception;

        } catch (RestClientException exception) {
            throw exception;
        }
    }

    private <R> RestClient.RequestBodySpec createRequest(HttpMethod method,String url,R requestBody,HttpHeaders headers) {
        RestClient.RequestBodySpec request = restClient
                                                .method(method)
                                                .uri(url)
                                                .headers(httpHeaders -> applyHeaders(httpHeaders,headers));
        if (requestBody != null) {
            request.contentType(MediaType.APPLICATION_JSON).body(requestBody);
        }
        return request;
    }

    private void handleErrorResponse(HttpMethod method,String url,ClientHttpResponse response) throws IOException {
        String responseBody;
        try {
            responseBody = response.getBody().toString();
        } catch (Exception ignored) {
            responseBody = "Unable to read response body";
        }
        throw new HttpClientException("HTTP request failed: " + method + " " + url, response.getStatusCode().value(), responseBody);
    }

    private void applyHeaders(HttpHeaders target, HttpHeaders additional) {
        target.set(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE);
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
        return statusCode == 408
                || statusCode == 429
                || statusCode == 500
                || statusCode == 502
                || statusCode == 503
                || statusCode == 504;
    }

    private long calculateNextDelay(long currentDelay) {
        double multiplier = properties.getRetry().getMultiplier();
        long maxDelay     = properties.getRetry().getMaxDelayMs();
        long nextDelay    = (long) (currentDelay * multiplier);
        return Math.min(nextDelay, maxDelay);
    }

    private void sleep(long delay) {
        if (delay <= 0) {
            return;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new HttpRetryException("HTTP retry interrupted",exception);
        }
    }
}