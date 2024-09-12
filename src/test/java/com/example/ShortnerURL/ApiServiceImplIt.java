package com.example.ShortnerURL;

import com.example.ShortnerURL.exceptions.ShortLinkNotFoundException;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import com.example.ShortnerURL.repositories.ShortLinkRepository;
import com.example.ShortnerURL.service.ApiService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ApiServiceImplIt extends PostgresIntegrationTest {
    @Autowired
    private ApiService apiService;
    private static final String EXISTING_SHORT_LINK_CODE = "abc123";
    private static final String NON_EXISTING_SHORT_LINK_CODE = "aba123";
    @BeforeEach
    void setUp(){
        ShortLinkEntity shortLinkEntity = ShortLinkEntity.builder()
                .longLink("http://example.com")
                .shortLinkCode("abc123")
                .build();
        shortLinkRepository.save(shortLinkEntity);
    }

    @Test
    void shouldGetShortLink() {
        ShortLinkDto result = apiService.getShortLink("abc123");

        assertNotNull(result);
        assertEquals("abc123", result.getShortLinkCode());
        assertEquals("http://example.com", result.getLongLink());
    }

    @Test
    public void shouldGetAllShortLinks_existingData() {
        List<ShortLinkDto> shortLinkDtos = apiService.getAllShortLinks();

        assertFalse(shortLinkDtos.isEmpty());
        assertTrue(shortLinkDtos.stream().anyMatch(dto->"abc123".equals(dto.getShortLinkCode())));
    }
    @Test
    public void shouldDeleteShortLink(){
        apiService.deleteShortLink(EXISTING_SHORT_LINK_CODE);
        assertFalse(shortLinkRepository.existsByShortLinkCode(EXISTING_SHORT_LINK_CODE));
    }
    @Test
    public void shouldDeleteException(){
        assertThrows(ShortLinkNotFoundException.class, () -> {
            apiService.deleteShortLink(NON_EXISTING_SHORT_LINK_CODE);
        });
    }
}