package com.loan_org.customer_management.http.exception;

import lombok.Getter;

@Getter
public class HttpClientException extends RuntimeException {

    private final int    statusCode;
    private final String responseBody;

    public HttpClientException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
        this.responseBody = null;
    }
    
}