package com.example.ShortnerURL.service;

import com.example.ShortnerURL.exceptions.ApplicationExceptions;
import com.example.ShortnerURL.exceptions.ShortLinkNotFoundException;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import com.example.ShortnerURL.exceptions.InvalidUrlException;
import com.example.ShortnerURL.repositories.ShortLinkRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;



import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final ShortLinkRepository shortLinkRepository;
    private final ModelMapper modelMapper;
    private static final String DOMAIN = "example.com/";

    public List<ShortLinkDto> getAllShortLinks() {
        return shortLinkRepository.findAll()
                .stream()
                .map(ApiService::convertToDto)
                .toList();
    }
    private static ShortLinkDto convertToDto(ShortLinkEntity shortLinkEntity){
        return ShortLinkDto.builder()
                .shortLinkCode(shortLinkEntity.getShortLinkCode())
                .shortLink(DOMAIN + shortLinkEntity.getShortLinkCode())
                .longLink(shortLinkEntity.getLongLink())
                .build();
    }
    @Transactional
    public void deleteShortLink(String shortLinkCode) throws ShortLinkNotFoundException {
        if (!shortLinkRepository.existsByShortLinkCode(shortLinkCode)) {
            throw new ShortLinkNotFoundException(shortLinkCode);
        }
        shortLinkRepository.deleteByShortLinkCode(shortLinkCode);
    }


    public ShortLinkDto getShortLink(String shortLinkCode) {

        return shortLinkRepository.findByShortLinkCode(shortLinkCode)
                .stream()
                .findFirst()
                .map(ApiService::convertToDto)
                .orElseThrow(() -> new ShortLinkNotFoundException(shortLinkCode));
    }

    public ShortLinkDto createShortLink(String longLink) throws ApplicationExceptions {
        String shortLinkCode = "";
        if (!validateURL(longLink)) {
            throw new InvalidUrlException(longLink);
        }
        do {
            shortLinkCode = RandomStringUtils.randomAlphanumeric(6);
        } while (shortLinkRepository.existsByShortLinkCode(shortLinkCode));

        ShortLinkEntity shortLinkEntity = ShortLinkEntity.builder()
                .shortLinkCode(shortLinkCode)
                .longLink(longLink)
                .build();
        ShortLinkEntity savedEntity = shortLinkRepository.save(shortLinkEntity);
        return convertToDto(savedEntity);
    }

    private static boolean validateURL(String longLink) {
        try {
            new URL(longLink).toURI();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}