package com.tracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author by Raj Aryan,
 * created on 27/09/2024
 */
public record UserRegistrationDto(
        @NotEmpty(message = "User Name must not be empty")
        String userName,

        String mobileNumber,

        @Email(message = "Invalid email format")
        @NotEmpty(message = "User email must not be empty")
        String userEmail,

        @NotEmpty(message = "User password must not be empty")
        String userPassword,

        @NotEmpty(message = "User role must not be empty")
        String userRole
) {
}
