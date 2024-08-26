package com.example.ShortnerURL.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component

public class RandomAlphanumericProvider {
    public String getNewShortLinkCode(){
        String shortLinkCode = RandomStringUtils.randomAlphanumeric(6);
        return shortLinkCode;
    }
}
