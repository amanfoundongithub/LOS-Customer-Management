package com.loan_org.customer_management.exception;

public class CustomerAlreadyExistsException
        extends RuntimeException {

    public CustomerAlreadyExistsException(
            String message
    ) {
        super(message);
    }
}