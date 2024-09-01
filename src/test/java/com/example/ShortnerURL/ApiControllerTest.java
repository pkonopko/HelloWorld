package com.example.ShortnerURL;

import com.example.ShortnerURL.controller.ApiController;
import com.example.ShortnerURL.models.dto.ShortLinkDto;
import com.example.ShortnerURL.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        this.mockMvc.perform(get("/shortLink/{shortLinkCode}","abc123" )).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(asJsonString(shortLinkDto1)));
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
