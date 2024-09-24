package com.tracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Raj Aryan,
 * created on 24/09/2024
 */
@Getter
@AllArgsConstructor
public enum ErrorType {

    UNKNOWN(0, "Unknown"),
    BAD_REQUEST(1, "Bad Request"),
    INTERNAL_SERVER(2, "Internal Server"),
    EXTERNAL_SERVER(3, "External Server"),
    NOT_FOUND(4, "Not Found"),
    UNPROCESSABLE_ENTITY(5, "Unprocessable Entity"),
    VALIDATION(6, "Validation");

    private final Integer index;
    private final String value;
}
