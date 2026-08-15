package com.loan_org.customer_management.exception;

public class InvalidIdentificationException
        extends RuntimeException {

    public InvalidIdentificationException(
            String message
    ) {
        super(message);
    }
}