package com.tracker.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author by Raj Aryan,
 * created on 24/09/2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Metric {
        public static final String EXECUTION_START_TIME = "executionStartTime";
        public static final String EXECUTION_TOTAL_TIME = "executionTotalTime";
        public static final String TRACE_ID = "traceId";
        public static final String TRANSACTION_ID = "transactionId";
        public static final String REQUEST_URI = "requestUri";
        public static final String HTTP_METHOD = "httpMethod";
        public static final String CONTROLLER_METHOD = "controllerMethod";
        public static final String HTTP_STATUS_CODE = "httpStatusCode";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RequestHeader {
        public static final String REQUEST_ID = "x-request-id";
        public static final String SD_TOKEN = "x-sd-token";
        public static final String AUTHORIZATION = "Authorization";

    }
}
