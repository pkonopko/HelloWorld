package com.example.ShortnerURL.controller;

import com.example.ShortnerURL.exceptions.ApplicationExceptions;
import com.example.ShortnerURL.models.dto.CreateShortLinkRequestDto;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/shortLink")
@RequiredArgsConstructor
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final ApiService apiService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShortLinkDto createShortLink(@RequestBody CreateShortLinkRequestDto createShortLinkRequestDto) throws ApplicationExceptions {
        return apiService.createShortLink(createShortLinkRequestDto.getLongLink());
    }

    @GetMapping()
    public ResponseEntity<List<ShortLinkDto>> getShortLinksAvailable() {
        return ResponseEntity.ok(apiService.getAllShortLinks());
    }

    @GetMapping("/{shortLinkCode}")
    public ResponseEntity<ShortLinkDto> getShortLink(@PathVariable String shortLinkCode) {
        return ResponseEntity.ok(apiService.getShortLink(shortLinkCode));
    }
    @DeleteMapping("/{shortLinkCode}")
    public ResponseEntity<String> deleteShortLink(@PathVariable String shortLinkCode){
        apiService.deleteShortLink(shortLinkCode);
        return ResponseEntity.ok("Link deleted");
    }
}
