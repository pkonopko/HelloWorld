package com.example.ShortnerURL.exceptions;

import org.springframework.http.HttpStatus;

public class ShortLinkNotFoundException extends ApplicationExceptions {
    public ShortLinkNotFoundException(Long id){
super("ShortLink with id " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
