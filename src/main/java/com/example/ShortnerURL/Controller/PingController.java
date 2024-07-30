package com.example.ShortnerURL.Controller;
import com.example.ShortnerURL.Exceptions.ApplicationExceptions;
import com.example.ShortnerURL.WebDto.SuccessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PingController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/ping")
    public SuccessResponseDto ping() {
        try {
            jdbcTemplate.queryForObject("SELECT current_timestamp;", String.class);
            return SuccessResponseDto.builder()
                    .status("ok")
                    .build();
        } catch (Exception e) {
            throw new ApplicationExceptions("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}