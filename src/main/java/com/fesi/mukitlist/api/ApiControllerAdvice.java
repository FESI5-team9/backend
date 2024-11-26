package com.fesi.mukitlist.api;

import java.net.BindException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fesi.mukitlist.api.service.ApiResponse;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }
}
