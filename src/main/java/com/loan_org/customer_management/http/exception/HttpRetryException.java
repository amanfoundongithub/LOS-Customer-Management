package com.loan_org.customer_management.http.exception;

public class HttpRetryException extends RuntimeException {

    public HttpRetryException(String message, Throwable cause) {
        super(message, cause);
    }
    
}