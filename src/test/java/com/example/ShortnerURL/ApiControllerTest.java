package com.example.ShortnerURL;

import com.example.ShortnerURL.controller.ApiController;
import com.example.ShortnerURL.models.dto.CreateShortLinkRequestDto;
import com.example.ShortnerURL.models.dto.DeleteShortLinkResultDTO;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;



@WebMvcTest(ApiController.class)
public class ApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ApiService apiService;


    @Test
    void shouldGetShortLink() throws Exception {
        ShortLinkDto shortLinkDto1 = ShortLinkDto.builder()
                .longLink("https://news.sky.com/")
                .shortLinkCode("abc123")
                .build();
        when(apiService.getShortLink("abc123")).thenReturn(shortLinkDto1);
        this.mockMvc.perform(get("/shortLink/{shortLinkCode}", "abc123")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(shortLinkDto1)));
    }
    @Test
    void shouldGetShortLinksAvailable() throws Exception {
        ShortLinkDto shortLinkDto1 = ShortLinkDto.builder()
                .longLink("https://news.sky.com/")
                .shortLinkCode("abc123")
                .build();
        ShortLinkDto shortLinkDto2 = ShortLinkDto.builder()
                .longLink("https://www.skysports.com/")
                .shortLinkCode("cab321")
                .build();
        List<ShortLinkDto> expectedResults = Arrays.asList(shortLinkDto1, shortLinkDto2);
        when(apiService.getAllShortLinks()).thenReturn(expectedResults);
        this.mockMvc.perform(get("/shortLink", "abc123", "cab321")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expectedResults)));

    }
    @Test
    void shouldDeleteShortLink() throws Exception{

        DeleteShortLinkResultDTO deleteShortLinkResultDTO = DeleteShortLinkResultDTO.builder()
                .shortLinkCode("abc123")
                .result("Link deleted")
                .build();
        when(apiService.deleteShortLink("abc123")).thenReturn(deleteShortLinkResultDTO);
        this.mockMvc.perform(delete("/shortLink/{shortLinkCode}", "abc123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"shortLinkCode\": \"abc123\", \"result\": \"Link deleted\"}"));

    }
    @Test
    void shouldCreateShortLink() throws Exception {
        CreateShortLinkRequestDto createShortLinkRequestDto = CreateShortLinkRequestDto.builder()
                .longLink("https://www.skysports.com/")
                .build();
        ShortLinkDto shortLinkDto2 = ShortLinkDto.builder()
                .longLink("https://www.skysports.com/")
                .shortLinkCode("cab321")
                .build();
        String createShortLinkRequestJson = asJsonString(createShortLinkRequestDto);

        when(apiService.createShortLink("https://www.skysports.com/")).thenReturn(shortLinkDto2);
        this.mockMvc.perform(post("/shortLink","https://www.skysports.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createShortLinkRequestJson))
                .andDo(print()).
                andExpect(status().isCreated())
                .andExpect(content().json(asJsonString(shortLinkDto2)));
    }
    @Test
    void shouldRedirectToOriginalUrl() throws Exception{
        RedirectView redirectView = new RedirectView("https://www.skysports.com/");
        when(apiService.redirectToOriginalUrl("cab321")).thenReturn(redirectView);
        this.mockMvc.perform(get("/{shortLinkCode}", "cab321")).andDo(print()).andExpect(status().isFound())
                .andExpect(redirectedUrl("https://www.skysports.com/"));

    }
    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}