package com.example.ShortnerURL.controller;

import com.example.ShortnerURL.exceptions.ApplicationExceptions;
import com.example.ShortnerURL.models.dto.CreateShortLinkRequestDto;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final ApiService apiService;

    @PostMapping("/shortLink")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortLinkDto createShortLink(@RequestBody CreateShortLinkRequestDto createShortLinkRequestDto) throws ApplicationExceptions {
        return apiService.createShortLink(createShortLinkRequestDto.getLongLink());
    }

    @GetMapping("/shortLink")
    public ResponseEntity<List<ShortLinkDto>> getShortLinksAvailable() {
        return ResponseEntity.ok(apiService.getAllShortLinks());
    }

    @GetMapping("/shortLink/{shortLinkCode}")
    public ResponseEntity<ShortLinkDto> getShortLink(@PathVariable String shortLinkCode) {
        return ResponseEntity.ok(apiService.getShortLink(shortLinkCode));
    }
    @DeleteMapping("/shortLink/{shortLinkCode}")
    public ResponseEntity<String> deleteShortLink(@PathVariable String shortLinkCode){
        apiService.deleteShortLink(shortLinkCode);
        return ResponseEntity.ok("Link deleted");
    }
    @GetMapping("/{shortLinkCode}")
    @ResponseStatus(HttpStatus.FOUND)
    public RedirectView redirectToOriginalUrl(@PathVariable String shortLinkCode){
        return apiService.redirectToOriginalUrl(shortLinkCode);
    }
    @GetMapping({"/404", "/404.html", "/**"})
    public ResponseEntity<Resource> notFoundPage() {
        Resource resource = new ClassPathResource("static/404.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }
}