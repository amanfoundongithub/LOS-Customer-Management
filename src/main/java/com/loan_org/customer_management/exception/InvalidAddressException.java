package com.loan_org.customer_management.exception;

public class InvalidAddressException
        extends RuntimeException {

    public InvalidAddressException(
            String message
    ) {
        super(message);
    }
}