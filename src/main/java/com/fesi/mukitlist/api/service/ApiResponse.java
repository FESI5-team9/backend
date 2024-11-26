package com.fesi.mukitlist.api.service;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private int code;
    private String parameter;
    private String message;
    private T data;

    public ApiResponse(HttpStatus httpStatus, String parameter, String message, T data) {
        this.code = httpStatus.value();
        this.parameter = parameter;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message) {
        return of(httpStatus, message);
    }
}
