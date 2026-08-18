package com.loan_org.customer_management.utils;

public class ErrorTruncator {

    private static int MAX_DEFAULT_LENGTH = 2000;

    private ErrorTruncator() {
        throw new UnsupportedOperationException("The instantiation is not supported!");
    }

    public static String truncateError(String error, int maxSize) {
        if(error == null) {
            return null;
        }
        if(error.length() <= maxSize) {
            return error;
        }
        return error.substring(0, maxSize);
    }

    public static String truncateError(String error) {
        return truncateError(error, MAX_DEFAULT_LENGTH);
    }

}
