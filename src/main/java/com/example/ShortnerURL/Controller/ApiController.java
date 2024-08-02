package com.example.ShortnerURL.Controller;

import com.example.ShortnerURL.Entity.ShortLinkEntity;
import com.example.ShortnerURL.Service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shortLink")
@RequiredArgsConstructor
public class ApiController {
    ApiService apiService;

    @GetMapping()
    public ResponseEntity<List<ShortLinkEntity>> getShortLinksAvailable() {
        return ResponseEntity.ok(apiService.getShortLinksAvailable());
    }

    @GetMapping("/{shortLinkCode}")
    public ResponseEntity<ShortLinkEntity> getShortLink(@RequestParam String shortLinkCode) {
        return ResponseEntity.ok(apiService.getShortLink());
    }
    @PostMapping()
    public ResponseEntity<ShortLinkEntity> createShortLink(@RequestParam String longLink) {
        return ResponseEntity.ok(apiService.createShortLink());
    }
    @DeleteMapping("/{shortLinkCode}")
    public ResponseEntity<String> deleteLink(@PathVariable String shortLinkCode){
    apiService.deleteLink(shortLinkCode);
    return ResponseEntity.ok("Link deleted");
    }
}
