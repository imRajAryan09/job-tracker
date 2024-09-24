package com.tracker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Pawan Saini
 * Created on 02/11/23.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FailureResponse<T> extends BaseResponse<T> {

    private static final boolean FAILURE_STATUS = false;

    public FailureResponse() {
        this.status = FAILURE_STATUS;
    }

    public FailureResponse(T data) {
        this.status = FAILURE_STATUS;
        this.data = data;
    }

    public FailureResponse(List<ErrorResponse> errors) {
        this.status = FAILURE_STATUS;
        this.errors = errors;
    }

    public FailureResponse(T data, List<ErrorResponse> errors) {
        this.status = FAILURE_STATUS;
        this.data = data;
        this.errors = errors;
    }
}
