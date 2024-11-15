package com.example.ShortnerURL.service;

import com.example.ShortnerURL.exceptions.ApplicationExceptions;
import com.example.ShortnerURL.exceptions.ShortLinkNotFoundException;
import com.example.ShortnerURL.models.dto.DeleteShortLinkResultDTO;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import com.example.ShortnerURL.exceptions.InvalidUrlException;
import com.example.ShortnerURL.repositories.ShortLinkRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;


import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final ShortLinkRepository shortLinkRepository;
    private final RandomAlphanumericProvider randomAlphanumericProvider;
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private static final String DOMAIN = "example.com/";
    private static final String NOT_FOUND_PAGE = "404";

    public List<ShortLinkDto> getAllShortLinks() {
        return shortLinkRepository.findAll()
                .stream()
                .map(ApiService::convertToDto)
                .toList();
    }

    private static ShortLinkDto convertToDto(ShortLinkEntity shortLinkEntity) {
        return ShortLinkDto.builder()
                .shortLinkCode(shortLinkEntity.getShortLinkCode())
                .shortLink(DOMAIN + shortLinkEntity.getShortLinkCode())
                .longLink(shortLinkEntity.getLongLink())
                .build();
    }

    @Transactional
    public DeleteShortLinkResultDTO deleteShortLink(String shortLinkCode) throws ShortLinkNotFoundException {
        //write a code that calls the database once
        int deletedCount = shortLinkRepository.deleteByShortLinkCode(shortLinkCode);
        if (deletedCount == 0) {
            throw new ShortLinkNotFoundException(shortLinkCode);
        }
        return DeleteShortLinkResultDTO.builder()
                .result("Link deleted")
                .shortLinkCode(shortLinkCode)
                .build();


    }


    public ShortLinkDto getShortLink(String shortLinkCode) {

        return shortLinkRepository.findByShortLinkCode(shortLinkCode)
                .stream()
                .findFirst()
                .map(ApiService::convertToDto)
                .orElseThrow(() -> new ShortLinkNotFoundException(shortLinkCode));
    }
    @Transactional
    public ShortLinkDto createShortLink(String longLink) throws ApplicationExceptions {
        String shortLinkCode = "";
        if (!validateURL(longLink)) {
            throw new InvalidUrlException(longLink);
        }
        do {
            shortLinkCode = randomAlphanumericProvider.getNewShortLinkCode();
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
            logger.error("URL validation fail: ", e);
            return false;
        }
        return true;
    }

    public RedirectView redirectToOriginalUrl(String shortLinkCode) throws ShortLinkNotFoundException{
        List<ShortLinkEntity> shortLinkEntities = shortLinkRepository.findByShortLinkCode(shortLinkCode);
        String longLink = shortLinkEntities
                .stream()
                .findFirst()
                .map(ShortLinkEntity::getLongLink)
                .orElse(NOT_FOUND_PAGE);
        return new RedirectView(longLink);
    }
}