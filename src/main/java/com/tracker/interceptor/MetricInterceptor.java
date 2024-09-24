package com.tracker.interceptor;

import com.tracker.constants.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Raj Aryan
 * Created on 24/09/24.
 */

@Slf4j
@Component
public class MetricInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        request.setAttribute(AppConstant.Metric.EXECUTION_START_TIME, Instant.now().toEpochMilli());
        ThreadContext.put(AppConstant.Metric.REQUEST_URI, request.getRequestURI());
        ThreadContext.put(AppConstant.Metric.HTTP_METHOD, request.getMethod());
        setHeadersToThreadContext(request);
        log.info("Intercepted Request :: httpMethod:{}, requestUri:{} and queryParams:{}", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex) {
        long executionStartTime = (long) request.getAttribute(AppConstant.Metric.EXECUTION_START_TIME);
        double executionTime = (Instant.now().toEpochMilli() - executionStartTime) / 1000D;
        String httpStatusCode = String.valueOf(response.getStatus());
        ThreadContext.put(AppConstant.Metric.HTTP_STATUS_CODE, httpStatusCode);
        ThreadContext.put(AppConstant.Metric.EXECUTION_TOTAL_TIME, String.valueOf(executionTime));
        log.info("Completed Request :: httpMethod: {}, requestUri: {}, queryParams: {}, httpStatusCode:{} and executionTime: {}s", request.getMethod(), request.getRequestURI(), request.getQueryString(), httpStatusCode, executionTime);

        ThreadContext.clearAll();
    }

    private void setHeadersToThreadContext(HttpServletRequest request) {
        String requestId = request.getHeader(AppConstant.RequestHeader.REQUEST_ID);
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        ThreadContext.put(AppConstant.RequestHeader.REQUEST_ID, requestId);
    }
}
