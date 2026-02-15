package dev.bored.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bored.profile.dto.ExperienceDTO;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.config.SecurityConfig;
import dev.bored.profile.service.ExperienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExperienceController.class)
@Import(SecurityConfig.class)
@WithMockUser
class ExperienceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExperienceService experienceService;

    private final ExperienceDTO testDTO = ExperienceDTO.builder()
            .experienceId(1L).profileId(1L).id("googol")
            .company("Googol").role("Senior Dev").roleStyle("frontend")
            .startDate(LocalDate.of(2023, 1, 15))
            .endDate(LocalDate.of(2025, 6, 30))
            .sortOrder(1)
            .build();

    @Test
    void getExperiences_ShouldReturnList() throws Exception {
        when(experienceService.getExperiencesByProfileId(1L)).thenReturn(List.of(testDTO));

        mockMvc.perform(get("/api/v1/experiences").param("profileId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("Googol"));
    }

    @Test
    void getExperiences_ShouldReturnEmpty_WhenNone() throws Exception {
        when(experienceService.getExperiencesByProfileId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/experiences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getExperienceById_ShouldReturnDTO() throws Exception {
        when(experienceService.getExperienceById(1L)).thenReturn(testDTO);

        mockMvc.perform(get("/api/v1/experiences/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Googol"));
    }

    @Test
    void getExperienceById_ShouldReturn404_WhenNotFound() throws Exception {
        when(experienceService.getExperienceById(999L))
                .thenThrow(new GenericException("Experience not found with id: 999", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/experiences/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addExperience_ShouldReturnCreated() throws Exception {
        when(experienceService.addExperience(any(ExperienceDTO.class))).thenReturn(testDTO);

        mockMvc.perform(post("/api/v1/experiences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Googol"));
    }

    @Test
    void updateExperience_ShouldReturnUpdated() throws Exception {
        when(experienceService.updateExperience(eq(1L), any(ExperienceDTO.class))).thenReturn(testDTO);

        mockMvc.perform(put("/api/v1/experiences/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Googol"));
    }

    @Test
    void deleteExperience_ShouldReturnTrue() throws Exception {
        when(experienceService.deleteExperience(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/experiences/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteExperience_ShouldReturn404_WhenNotFound() throws Exception {
        when(experienceService.deleteExperience(999L))
                .thenThrow(new GenericException("Experience not found with id: 999", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/v1/experiences/999"))
                .andExpect(status().isNotFound());
    }
}
