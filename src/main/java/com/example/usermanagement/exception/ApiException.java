package com.example.usermanagement.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiException {

    private final HttpStatus httpStatus;
    private final LocalDateTime localDateTime;
    private final String message;

    public ApiException(HttpStatus httpStatus,String message) {
        this.httpStatus = httpStatus;
        this.localDateTime = LocalDateTime.now();
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getMessage() {
        return message;
    }
}
