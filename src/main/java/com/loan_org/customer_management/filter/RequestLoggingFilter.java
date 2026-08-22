package com.loan_org.customer_management.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.loan_org.customer_management.configuration.properties.MdcProperties;
import com.loan_org.customer_management.configuration.properties.RequestLoggingProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class RequestLoggingFilter
        extends OncePerRequestFilter {

    private final RequestLoggingProperties properties;
    private final MdcProperties mdcProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!properties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.nanoTime();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString =
                properties.isIncludeQueryString()
                        ? request.getQueryString()
                        : null;
        String correlationId =
                properties.isIncludeCorrelationId()
                        ? getCorrelationId()
                        : null;
        String traceId =
                properties.isIncludeTraceId()
                        ? getTraceId()
                        : null;
        log.info(
                "Incoming request | method={} uri={} query={} correlationId={} traceId={}",
                method,
                uri,
                queryString,
                correlationId,
                traceId
        );

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs =
                    (System.nanoTime() - startTime)
                            / 1_000_000;

            log.info(
                    "Completed request | method={} uri={} status={} durationMs={} correlationId={} traceId={}",
                    method,
                    uri,
                    response.getStatus(),
                    properties.isIncludeDuration()
                            ? durationMs
                            : null,
                    correlationId,
                    traceId
            );
        }
    }

    private String getCorrelationId() {
        return MDC.get(mdcProperties.getCorrelation().getMdcKey());
    }

    private String getTraceId() {
        return MDC.get(mdcProperties.getTrace().getMdcKey());
    }
}