package com.loan_org.customer_management.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcHeaderFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID = "correlationId";
    public static final String TRACE_ID = "traceId";

    public static final String CORRELATION_HEADER =
            "X-Correlation-Id";

    public static final String TRACE_HEADER =
            "X-Trace-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String correlationId =
                getOrGenerate(
                        request.getHeader(CORRELATION_HEADER)
                );

        String traceId =
                getOrGenerate(
                        request.getHeader(TRACE_HEADER)
                );

        try {

            MDC.put(
                    CORRELATION_ID,
                    correlationId
            );

            MDC.put(
                    TRACE_ID,
                    traceId
            );

            response.setHeader(
                    CORRELATION_HEADER,
                    correlationId
            );

            response.setHeader(
                    TRACE_HEADER,
                    traceId
            );

            filterChain.doFilter(
                    request,
                    response
            );

        } finally {

            MDC.remove(CORRELATION_ID);
            MDC.remove(TRACE_ID);
        }
    }

    private String getOrGenerate(
            String value
    ) {

        if (StringUtils.hasText(value)) {
            return value.trim();
        }

        return UUID.randomUUID().toString();
    }
}