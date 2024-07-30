package com.example.ShortnerURL.Exceptions.globalExceptionHandler;

import com.example.ShortnerURL.Exceptions.ApplicationExceptions;
import com.example.ShortnerURL.WebDto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationExceptions.class)
    public final ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationExceptions ex){
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status("error")
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<?>HandleException(Throwable ex){
        return ResponseEntity.internalServerError().body("Unknown error");
    }
}
