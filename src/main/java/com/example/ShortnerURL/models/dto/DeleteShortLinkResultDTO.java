package com.example.ShortnerURL.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteShortLinkResultDTO {
    String result;
    String shortLinkCode;
}
