package com.example.ShortnerURL.models.mappings;

import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class ShortLinkEntityToShortLinkDto implements Converter<ShortLinkEntity, ShortLinkDto> {
    @Override
    public ShortLinkDto convert(MappingContext<ShortLinkEntity, ShortLinkDto> mappingContext) {
        ShortLinkEntity shortLinkEntity = mappingContext.getSource();
        return ShortLinkDto.builder()
                .shortLink(shortLinkEntity.getShortLinkCode())
                .shortLinkCode(shortLinkEntity.getShortLinkCode())
                .longLink(shortLinkEntity.getLongLink())
                .build();
    }
}

