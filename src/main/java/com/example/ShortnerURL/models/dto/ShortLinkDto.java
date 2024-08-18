package com.example.ShortnerURL.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkDto {
    String shortLinkCode;
    String shortLink;
    String longLink;
}
