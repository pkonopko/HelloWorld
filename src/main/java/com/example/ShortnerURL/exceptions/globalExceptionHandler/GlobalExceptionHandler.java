package com.example.ShortnerURL.exceptions.globalExceptionHandler;

import com.example.ShortnerURL.exceptions.ApplicationExceptions;
import com.example.ShortnerURL.webDto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApplicationExceptions.class)
    public final ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationExceptions ex){
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .build();
        logger.error("Application exception: ", ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ErrorResponseDto>HandleException(Throwable ex){
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("Unknown error")
                .build();
        logger.error("Unknown error: ", ex);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
