package com.example.ShortnerURL;

import ch.qos.logback.core.net.AbstractSSLSocketAppender;
import com.example.ShortnerURL.exceptions.InvalidUrlException;
import com.example.ShortnerURL.exceptions.ShortLinkNotFoundException;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import com.example.ShortnerURL.repositories.ShortLinkRepository;
import com.example.ShortnerURL.service.ApiService;
import com.example.ShortnerURL.service.RandomAlphanumericProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.view.RedirectView;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ShortnerUrlApplicationTests {
	@MockBean
	private ShortLinkRepository shortLinkRepository;
	@MockBean
	private RandomAlphanumericProvider randomAlphanumericProvider;
	private static final String DOMAIN = "example.com/";

	private ApiService apiService;
	private ShortLinkEntity shortLinkEntity;
	private ShortLinkDto shortLinkDto;

	@BeforeEach
	void setup() {
		apiService = new ApiService(shortLinkRepository, randomAlphanumericProvider);
		shortLinkEntity = ShortLinkEntity.builder()
				.id(1L)
				.longLink("https://news.sky.com/")
				.shortLinkCode("NjxQpL")
				.build();
		shortLinkDto = ShortLinkDto.builder()
				.shortLinkCode("NjxQpL")
				.shortLink(DOMAIN + "NjxQpL")
				.longLink("https://news.sky.com/")
				.build();

	}

	@Test
	void shouldGetAllShortLinks() {
		when(shortLinkRepository.findAll()).thenReturn(List.of(shortLinkEntity));
		List<ShortLinkDto> result = apiService.getAllShortLinks();
		assertEquals(1, result.size());
		ShortLinkDto actualDto = result.get(0);
		assertEquals(shortLinkDto.getShortLink(), actualDto.getShortLink());
		assertEquals(shortLinkDto.getShortLinkCode(), actualDto.getShortLinkCode());
		assertEquals(shortLinkDto.getLongLink(), actualDto.getLongLink());
	}

	@Test
	void shouldGetShortLink() {
		when(shortLinkRepository.findByShortLinkCode(shortLinkEntity.getShortLinkCode())).thenReturn(List.of(shortLinkEntity));
		ShortLinkDto result = apiService.getShortLink(shortLinkEntity.getShortLinkCode());
		assertNotNull(result);
		assertEquals(shortLinkEntity.getShortLinkCode(), result.getShortLinkCode());
		assertEquals(shortLinkEntity.getLongLink(), result.getLongLink());
		assertEquals(DOMAIN + shortLinkEntity.getShortLinkCode(), result.getShortLink());
		verify(shortLinkRepository, times(1)).findByShortLinkCode(shortLinkEntity.getShortLinkCode());
	}

	@Test
	void shouldThrowShortLinkNotFoundExceptionWhenShortLinkDoesNotExist() {
		when(shortLinkRepository.findByShortLinkCode(shortLinkEntity.getShortLinkCode())).thenReturn(List.of());
		assertThrows(ShortLinkNotFoundException.class, () -> apiService.getShortLink(shortLinkEntity.getShortLinkCode()));
	}

	@Test
	void shouldDeleteShortLink() {
		when(shortLinkRepository.existsByShortLinkCode(shortLinkEntity.getShortLinkCode())).thenReturn(true);
		apiService.deleteShortLink(shortLinkEntity.getShortLinkCode());
		verify(shortLinkRepository, times(1)).deleteByShortLinkCode(shortLinkEntity.getShortLinkCode());
	}

	@Test
	void shouldNotDeleteShortLink() {
		when(shortLinkRepository.existsByShortLinkCode(shortLinkEntity.getShortLinkCode())).thenReturn(false);
		assertThrows(ShortLinkNotFoundException.class, () -> apiService.deleteShortLink(shortLinkEntity.getShortLinkCode()));
	}

	@Test
	void shouldCreateAShortLink() {
		String longLink = "https://news.sky.com/";
		ShortLinkEntity savedEntity = ShortLinkEntity.builder()
				.id(1L)
				.longLink(longLink)
				.shortLinkCode("NjxQpL")
				.build();

		when(randomAlphanumericProvider.getNewShortLinkCode()).thenReturn("NjxQpL");
		ArgumentCaptor<ShortLinkEntity> shortLinkEntityArgument = ArgumentCaptor.forClass(ShortLinkEntity.class);
		when(shortLinkRepository.save(shortLinkEntityArgument.capture())).thenReturn(savedEntity);
		ShortLinkDto actualDto = apiService.createShortLink(longLink);
		assertEquals("NjxQpL", shortLinkEntityArgument.getValue().getShortLinkCode());
		assertEquals(longLink, shortLinkEntityArgument.getValue().getLongLink());
		assertEquals("NjxQpL", actualDto.getShortLinkCode());
		assertEquals(longLink, actualDto.getLongLink());
	}
	@Test
	void shouldCreateAShortLinkInvalidUrlException() {
		String longLink = "httwww.sport";
		assertThrows(InvalidUrlException.class, () -> apiService.createShortLink(longLink));
		verify(shortLinkRepository, times(0)).existsByShortLinkCode(any(String.class));
		verify(shortLinkRepository, times(0)).save(any(ShortLinkEntity.class));
	}
	@Test
	void shouldCreateAShortLinkAlreadyExistingCode() {
		String longLink = "https://news.sky.com/";
		String existingCode = "NjxQpL";
		String newCode = "NjxQpa";
		when(randomAlphanumericProvider.getNewShortLinkCode()).thenReturn(existingCode, newCode);
		when(shortLinkRepository.existsByShortLinkCode(existingCode)).thenReturn(true);
		when(shortLinkRepository.existsByShortLinkCode(newCode)).thenReturn(false);
		ArgumentCaptor<ShortLinkEntity> shortLinkEntityArgument = ArgumentCaptor.forClass(ShortLinkEntity.class);

		ShortLinkEntity savedEntity = ShortLinkEntity.builder()
				.id(1L)
				.longLink(longLink)
				.shortLinkCode(newCode)
				.build();

		when(shortLinkRepository.save(shortLinkEntityArgument.capture())).thenReturn(savedEntity);

		ShortLinkDto result = apiService.createShortLink(longLink);

		verify(shortLinkRepository).existsByShortLinkCode(existingCode);
		verify(shortLinkRepository).existsByShortLinkCode(newCode);
		verify(shortLinkRepository).save(any(ShortLinkEntity.class));

		assertEquals(newCode, shortLinkEntityArgument.getValue().getShortLinkCode());
		assertEquals(longLink, shortLinkEntityArgument.getValue().getLongLink());

		assertEquals(newCode, result.getShortLinkCode());
		assertEquals(longLink, result.getLongLink());
	}

		@Test
	void shouldRedirectToOriginalUrl() {
		when(shortLinkRepository.findByShortLinkCode(shortLinkEntity.getShortLinkCode())).thenReturn(List.of(shortLinkEntity));
		RedirectView result = apiService.redirectToOriginalUrl(shortLinkEntity.getShortLinkCode());
		assertEquals(shortLinkDto.getLongLink(), result.getUrl());
	}

	@Test
	void shouldRedirectToOriginalUrlNotFound() {
		when(shortLinkRepository.findByShortLinkCode(shortLinkEntity.getShortLinkCode())).thenReturn(Collections.emptyList());
		RedirectView result = apiService.redirectToOriginalUrl(shortLinkEntity.getShortLinkCode());
		assertEquals("404", result.getUrl());
	}
}