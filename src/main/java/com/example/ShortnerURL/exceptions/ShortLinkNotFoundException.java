package com.example.ShortnerURL.exceptions;

import org.springframework.http.HttpStatus;

public class ShortLinkNotFoundException extends ApplicationExceptions {
    public ShortLinkNotFoundException(String shortLinkCode){
super("ShortLink with short link code " + shortLinkCode + " not found", HttpStatus.NOT_FOUND);
    }
}
