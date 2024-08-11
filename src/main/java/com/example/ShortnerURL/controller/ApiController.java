package com.example.ShortnerURL.controller;

import com.example.ShortnerURL.exceptions.ApplicationExceptions;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import com.example.ShortnerURL.exceptions.InvalidUrlException;
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
    public ShortLinkDto createShortLink(@RequestBody String longLink) throws ApplicationExceptions {
        return apiService.createShortLink(longLink);
    }

    @GetMapping()
    public ResponseEntity<List<ShortLinkEntity>> getShortLinksAvailable() {
        return ResponseEntity.ok(apiService.getAllShortLinks());
    }

    @GetMapping("/{shortLinkCode}")
    public ResponseEntity<ShortLinkEntity> getShortLink(@RequestParam Long id) {
        return ResponseEntity.ok(apiService.getShortLink(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShortLink(@PathVariable Long id){
        apiService.deleteShortLink(id);
        return ResponseEntity.ok("Link deleted");
    }
}
