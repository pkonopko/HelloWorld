package com.example.ShortnerURL.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidUrlException extends ApplicationExceptions {
    public InvalidUrlException(){
        super("Invalid URL", HttpStatus.BAD_REQUEST);
    }
}
