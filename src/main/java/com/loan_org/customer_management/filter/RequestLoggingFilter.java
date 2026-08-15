package com.loan_org.customer_management.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class RequestLoggingFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long startTime =
                System.currentTimeMillis();

        String method =
                request.getMethod();

        String uri =
                request.getRequestURI();

        String queryString =
                request.getQueryString();

        String requestId =
                MDC.get(MdcHeaderFilter.CORRELATION_ID);

        log.info(
                "Incoming request | method={} uri={} query={} correlationId={}",
                method,
                uri,
                queryString,
                requestId
        );

        try {

            filterChain.doFilter(
                    request,
                    response
            );

        } finally {

            long duration =
                    System.currentTimeMillis()
                            - startTime;

            log.info(
                    "Completed request | method={} uri={} status={} durationMs={} correlationId={}",
                    method,
                    uri,
                    response.getStatus(),
                    duration,
                    requestId
            );
        }
    }
}