package com.loan_org.customer_management.exception;

public class InvalidCustomerStateException
        extends RuntimeException {

    public InvalidCustomerStateException(
            String message
    ) {
        super(message);
    }
}