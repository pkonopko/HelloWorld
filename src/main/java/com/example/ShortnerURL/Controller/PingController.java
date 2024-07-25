package com.example.ShortnerURL.Controller;
import com.example.ShortnerURL.WebDto.ErrorResponseDto;
import com.example.ShortnerURL.WebDto.SuccessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PingController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        try {
            jdbcTemplate.queryForObject("SELECT current_timestamp;", String.class);
            SuccessResponseDto successResponseDto = SuccessResponseDto.builder()
                    .status("status: ok")
                    .build();
            return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .status("status: error")
                    .build();
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}