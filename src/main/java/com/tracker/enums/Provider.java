package com.tracker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by Raj Aryan,
 * created on 23/11/2024
 */
@Getter
@AllArgsConstructor
public enum Provider {
    GOOGLE("google"),
    GITHUB("github"),
    FACEBOOK("facebook"),
    LINKEDIN("linkedin"),
    SELF("self");

    private final String providerId;
}
