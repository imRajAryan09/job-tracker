package com.tracker.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author by Raj Aryan,
 * created on 24/09/2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndPoint {

    // Ping Controller
    public static final String PING = "/ping";


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SecurePaths {
        public static final String SIGN_UP = "/auth/sign-up/**";
        public static final String SIGN_IN = "/auth/sign-in/**";
        public static final String LOGOUT = "/auth/logout/**";
        public static final String REFRESH_TOKEN = "/auth/refresh-token/**";
        public static final String API = "/api/**";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Authentication {
        public static final String BASE = "/auth";
        public static final String SIGN_UP = "/sign-up";
        public static final String SIGN_IN = "/sign-in";
        public static final String LOGOUT = "/logout";
        public static final String REFRESH_TOKEN = "/refresh-token";
    }
}
