package com.tracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Raj Aryan,
 * created on 27/09/2024
 */
@Getter
@AllArgsConstructor
public enum TokenType {
    BEARER("Bearer");

    private final String name;
}
