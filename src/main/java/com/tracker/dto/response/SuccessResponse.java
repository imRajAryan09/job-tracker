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
public class SuccessResponse<T> extends BaseResponse<T> {

    private static final boolean SUCCESS_STATUS = true;

    public SuccessResponse() {
        this.status = SUCCESS_STATUS;
    }

    public SuccessResponse(T data) {
        this.status = SUCCESS_STATUS;
        this.data = data;
    }

    public SuccessResponse(T data, PaginationResponse pagination) {
        this.status = SUCCESS_STATUS;
        this.data = data;
        this.pagination = pagination;
    }

    public SuccessResponse(List<String> messages) {
        this.status = SUCCESS_STATUS;
        this.messages = messages;
    }

    public SuccessResponse(T data, List<String> messages) {
        this.status = SUCCESS_STATUS;
        this.data = data;
        this.messages = messages;
    }

    public SuccessResponse(T data, PaginationResponse pagination, List<String> messages) {
        this.status = SUCCESS_STATUS;
        this.data = data;
        this.pagination = pagination;
        this.messages = messages;
    }
}
