package com.example.ShortnerURL.Controller;
import com.example.ShortnerURL.Exceptions.ApplicationExceptions;
import com.example.ShortnerURL.WebDto.PingResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PingController {
    private static final Logger logger = LoggerFactory.getLogger(PingController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/ping")
    public PingResponseDTO ping() {
        try {
            jdbcTemplate.queryForObject("SELECT current_timestamp;", String.class);
            return PingResponseDTO.builder()
                    .status("ok")
                    .build();
        } catch (Exception e) {
            logger.error("Error", e);
            throw new ApplicationExceptions("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}