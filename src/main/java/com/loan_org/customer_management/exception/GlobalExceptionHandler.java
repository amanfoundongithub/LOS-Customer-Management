package com.loan_org.customer_management.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    // ============================================================
    // CUSTOMER NOT FOUND
    // ============================================================

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFound(
            CustomerNotFoundException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "CUSTOMER_NOT_FOUND",
                exception.getMessage(),
                request,
                null
        );
    }


    // ============================================================
    // CUSTOMER ALREADY EXISTS
    // ============================================================

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerAlreadyExists(
            CustomerAlreadyExistsException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "CUSTOMER_ALREADY_EXISTS",
                exception.getMessage(),
                request,
                null
        );
    }


    // ============================================================
    // INVALID CUSTOMER STATE
    // ============================================================

    @ExceptionHandler(InvalidCustomerStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCustomerState(
            InvalidCustomerStateException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "INVALID_CUSTOMER_STATE",
                exception.getMessage(),
                request,
                null
        );
    }


    // ============================================================
    // INVALID ADDRESS
    // ============================================================

    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidAddress(
            InvalidAddressException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_ADDRESS",
                exception.getMessage(),
                request,
                null
        );
    }


    // ============================================================
    // INVALID IDENTIFICATION
    // ============================================================

    @ExceptionHandler(InvalidIdentificationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidIdentification(
            InvalidIdentificationException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_IDENTIFICATION",
                exception.getMessage(),
                request,
                null
        );
    }


    // ============================================================
    // BEAN VALIDATION
    // ============================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        Map<String, String> fieldErrors =
                new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Request validation failed",
                request,
                fieldErrors
        );
    }


    // ============================================================
    // CONSTRAINT VALIDATION
    // ============================================================

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {

        Map<String, String> fieldErrors =
                new LinkedHashMap<>();

        exception.getConstraintViolations()
                .forEach(violation ->
                        fieldErrors.put(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Request validation failed",
                request,
                fieldErrors
        );
    }


    // ============================================================
    // INVALID REQUEST PARAMETER TYPE
    // ============================================================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {

        String message =
                "Invalid value for parameter '"
                        + exception.getName()
                        + "'";

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_PARAMETER",
                message,
                request,
                null
        );
    }


    // ============================================================
    // ILLEGAL ARGUMENT
    // ============================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "INVALID_REQUEST",
                exception.getMessage(),
                request,
                null
        );
    }


    // ============================================================
    // UNEXPECTED EXCEPTION
    // ============================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {

        log.error(
                "Unexpected exception while processing request {} {}",
                request.getMethod(),
                request.getRequestURI(),
                exception
        );

        /*
         * Never expose the actual exception message to the client.
         *
         * It could contain:
         * - database information
         * - internal class names
         * - configuration details
         * - stack traces
         */
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected internal server error occurred",
                request,
                null
        );
    }


    // ============================================================
    // RESPONSE BUILDER
    // ============================================================

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request,
            Map<String, String> fieldErrors
    ) {

        String traceId =
                MDC.get("abcd");

        if (traceId == null || traceId.isBlank()) {
            traceId = MDC.get("abcd");
        }

        ApiErrorResponse response =
                ApiErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(status.value())
                        .error(error)
                        .message(message)
                        .path(request.getRequestURI())
                        .traceId(traceId)
                        .fieldErrors(fieldErrors)
                        .build();

        return ResponseEntity
                .status(status)
                .body(response);
    }
}