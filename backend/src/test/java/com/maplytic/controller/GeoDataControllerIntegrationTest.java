package com.maplytic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maplytic.dto.GeoDataResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class GeoDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("/api/geodata/nearest 정상 호출 테스트")
    void testGetNearestStation() throws Exception {
        double lat = 37.5665;
        double lon = 126.9780;

        String response = mockMvc.perform(get("/api/geodata/nearest")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GeoDataResponseDto result = objectMapper.readValue(response, GeoDataResponseDto.class);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isNotEmpty();
    }

    @Test
    void testSearchStations() throws Exception {
        mockMvc.perform(get("/api/geodata/search")
                .param("keyword", "강남"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testFilterStations() throws Exception {
        mockMvc.perform(get("/api/geodata/filter")
                .param("type", "가상"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetNearbyStations() throws Exception {
        mockMvc.perform(get("/api/geodata/nearby")
                .param("lat", "37.5665")
                .param("lon", "126.9780")
                .param("radius", "500"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

}
