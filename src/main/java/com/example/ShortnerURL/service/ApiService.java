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



import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final ShortLinkRepository shortLinkRepository;
    private final ModelMapper modelMapper;
    private static final String DOMAIN = "example.com/";

    public List<ShortLinkEntity> getAllShortLinks() {

        return shortLinkRepository.findAll();
    }

    public void deleteShortLink(Long id) throws ShortLinkNotFoundException {
        if(!shortLinkRepository.existsById(id)){
            throw new ShortLinkNotFoundException(id);
        }
         shortLinkRepository.deleteById(id);
    }

    public ShortLinkEntity getShortLink(Long id) {
        return shortLinkRepository.findById(id)
                .orElseThrow(() -> new ShortLinkNotFoundException(id));
    }

    public ShortLinkDto createShortLink(String longLink) throws ApplicationExceptions {
        String shortLinkCode = "";
//       if (!validateURL(longLink)) {
//            throw new InvalidUrlException();
//           }
            do {
                shortLinkCode = RandomStringUtils.randomAlphanumeric(6);
            } while (shortLinkRepository.existsByShortLinkCode(shortLinkCode));

            ShortLinkEntity shortLinkEntity = ShortLinkEntity.builder()
                    .shortLinkCode(shortLinkCode)
                    .longLink(longLink)
                    .build();
            ShortLinkEntity savedEntity = shortLinkRepository.save(shortLinkEntity);
            ShortLinkDto savedDto = modelMapper.map(savedEntity, ShortLinkDto.class);
            savedDto.setShortLinkCode(savedEntity.getShortLinkCode());
            savedDto.setShortLink(DOMAIN + savedDto.getShortLinkCode());
            return savedDto;
        }
    private static boolean validateURL(String longLink){
        try {
            new URL(longLink).toURI();
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }
}