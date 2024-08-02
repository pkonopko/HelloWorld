package com.example.ShortnerURL.WebDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class PingResponseDTO {
    String status;
}
