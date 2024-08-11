package com.example.ShortnerURL.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationExceptions extends RuntimeException {
    private final HttpStatus httpStatus;

    public ApplicationExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}