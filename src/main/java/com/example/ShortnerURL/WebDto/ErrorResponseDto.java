package com.example.ShortnerURL.webDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class ErrorResponseDto {
    String status;
    String message;
}
