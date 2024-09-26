package com.tracker.exception;

import com.tracker.dto.response.ErrorResponse;
import com.tracker.dto.response.FailureResponse;
import com.tracker.enums.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

/**
 * @author by Raj Aryan,
 * created on 24/09/2024
 */
@Slf4j
@RestControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureResponse<Object>> handleInternalException(Exception exception) {
        ExceptionType exceptionType = ExceptionType.INTERNAL_SERVER_EXCEPTION;
        return buildExceptionResponse(exception, exceptionType.getErrorCode(), exceptionType.getErrorType().getValue(), exceptionType.getHttpStatus());
    }

    private ResponseEntity<FailureResponse<Object>> buildExceptionResponse(
            Exception exception, Integer errorCode, String errorType, HttpStatus status) {
        log.error("Error in service :: {} :: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode)
                .errorType(errorType)
                .errorMessage(exception.getMessage())
                .build();
        FailureResponse<Object> response = new FailureResponse<>(Collections.singletonList(errorResponse));
        return new ResponseEntity<>(response, status);
    }
}
