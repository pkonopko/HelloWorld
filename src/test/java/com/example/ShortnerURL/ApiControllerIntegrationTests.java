package com.example.ShortnerURL;

import com.example.ShortnerURL.models.dto.CreateShortLinkRequestDto;
import com.example.ShortnerURL.models.dto.DeleteShortLinkResultDTO;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import org.h2.command.dml.Delete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApiControllerIntegrationTests extends PostgresIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        ShortLinkEntity shortLink1 = ShortLinkEntity.builder()
                .longLink("http://example.com")
                .shortLinkCode("abc123")
                .build();
        ShortLinkEntity shortLink2 = ShortLinkEntity.builder()
                .longLink("http://example.com")
                .shortLinkCode("xyz098")
                .build();
        shortLinkRepository.save(shortLink1);
        shortLinkRepository.save(shortLink2);
    }

    @Test
    void shouldGetShortLinksAvailable() {
        String url = "http://localhost:" + port + "/shortLink";
        ResponseEntity<ShortLinkDto[]> response = restTemplate.getForEntity(url, ShortLinkDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);

        List<ShortLinkDto> shortLinks = List.of(response.getBody());
        assertThat(shortLinks.get(0).getShortLinkCode()).isEqualTo("abc123");
        assertThat(shortLinks.get(1).getShortLinkCode()).isEqualTo("xyz098");
    }

    @Test
    void shouldGetShortLink() {
        String url = "http://localhost:" + port + "/shortLink/{shortLinkCode}";

        ResponseEntity<ShortLinkDto> response = restTemplate.getForEntity(url, ShortLinkDto.class, "abc123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNotNull();

        ShortLinkDto shortLinkDto = response.getBody();
        assertThat(shortLinkDto.getShortLinkCode()).isEqualTo("abc123");
        assertThat(shortLinkDto.getLongLink()).isEqualTo("http://example.com");
    }

    @Test
    void shouldDeleteShortLink() {
        
        String url = "http://localhost:" + port + "/shortLink/{shortLinkCode}";

        ResponseEntity<DeleteShortLinkResultDTO> response = restTemplate.exchange(url, HttpMethod.DELETE, null, DeleteShortLinkResultDTO.class, "abc123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResult()).isEqualTo("Link deleted");
        assertThat(response.getBody().getShortLinkCode()).isEqualTo("abc123");

        assertThat(shortLinkRepository.findByShortLinkCode("abc123")).isEmpty();
    }

    @Test
    void shouldCreateShortLink() {
        String url = "http://localhost:" + port + "/shortLink";

        CreateShortLinkRequestDto requestDto = new CreateShortLinkRequestDto();
        requestDto.setLongLink("http://example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpEntity<CreateShortLinkRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ShortLinkDto> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ShortLinkDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ShortLinkDto responseBody = response.getBody();
        assertThat(response.getBody()).isNotNull();
        assertThat(responseBody.getLongLink()).isEqualTo("http://example.com");

        List<ShortLinkEntity> savedEntities = shortLinkRepository.findByShortLinkCode(responseBody.getShortLinkCode());

        assertThat(savedEntities).isNotEmpty();
        ShortLinkEntity savedEntity = savedEntities.get(0);
        assertThat(savedEntity.getLongLink()).isEqualTo("http://example.com");
    }

    @Test
    void shouldRedirectURL() {
        String shortLinkCode = "abc123";
        String url = "http://localhost:" + port + "/" + shortLinkCode;
        // Set the request config in rest template to disable redirect
        HttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        RestTemplate restTemplateWithNoRedirect = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        ResponseEntity<Void> response = restTemplateWithNoRedirect.getForEntity(url, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        String expectedUrl = "http://example.com";
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isEqualTo(expectedUrl);
    }
}
