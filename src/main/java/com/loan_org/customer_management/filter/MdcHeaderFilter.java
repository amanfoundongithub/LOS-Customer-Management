package com.loan_org.customer_management.filter;

import com.loan_org.customer_management.config.mdc.MdcProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MdcHeaderFilter extends OncePerRequestFilter {

    private final MdcProperties properties;

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
        String correlationId = resolveCorrelationId(request);
        String traceId       = resolveTraceId(request);
        try {
            MDC.put(properties.getCorrelation().getMdcKey(), correlationId);
            MDC.put(properties.getTrace().getMdcKey(), traceId);
            addResponseHeaders(response, correlationId, traceId);
            filterChain.doFilter(request,response);
        } finally {
            MDC.remove(properties.getCorrelation().getMdcKey());
            MDC.remove(properties.getTrace().getMdcKey());
        }
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        var config = properties.getCorrelation();
        String incomingId = request.getHeader(config.getHeader());
        if (config.isAcceptIncoming() && isValidId(incomingId)) {
            return incomingId.trim();
        }
        if (config.isGenerateIfMissing()) {
            return UUID.randomUUID().toString();
        }
        return null;
    }

    private String resolveTraceId(HttpServletRequest request) {
        var config = properties.getTrace();
        String incomingId = request.getHeader(config.getHeader());
        if (config.isAcceptIncoming() && isValidId(incomingId)) {
            return incomingId.trim();
        }
        if (config.isGenerateIfMissing()) {
            return UUID.randomUUID().toString();
        }
        return null;
    }

    private boolean isValidId(String value) {
        return StringUtils.hasText(value) && 
        value.trim().length() <= properties.getValidation().getMaxIdLength();
    }

    private void addResponseHeaders(HttpServletResponse response, String correlationId, String traceId) {
        var responseConfig = properties.getResponse();
        if (responseConfig.isIncludeCorrelationId() && correlationId != null) {
            response.setHeader(properties.getCorrelation().getHeader(), correlationId);
        }
        if (responseConfig.isIncludeTraceId() && traceId != null) {
            response.setHeader(properties.getTrace().getHeader(), traceId);
        }
    }
}