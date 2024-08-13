package com.example.ShortnerURL.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidUrlException extends ApplicationExceptions {
    public InvalidUrlException(String url) {
        super("Invalid URL " + url, HttpStatus.BAD_REQUEST);
    }
}
