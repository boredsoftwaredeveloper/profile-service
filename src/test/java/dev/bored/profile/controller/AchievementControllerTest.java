package dev.bored.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bored.profile.dto.AchievementDTO;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.config.SecurityConfig;
import dev.bored.profile.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AchievementController.class)
@Import(SecurityConfig.class)
@WithMockUser
class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AchievementService achievementService;

    private final AchievementDTO testDTO = AchievementDTO.builder()
            .achievementId(1L).profileId(1L).id("coffee")
            .title("Coffee Consumed").progressPercent(95).variant("warning").sortOrder(1)
            .build();

    @Test
    void getAchievements_ShouldReturnList() throws Exception {
        when(achievementService.getAchievementsByProfileId(1L)).thenReturn(List.of(testDTO));

        mockMvc.perform(get("/api/v1/achievements").param("profileId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Coffee Consumed"));
    }

    @Test
    void getAchievements_ShouldReturnEmpty_WhenNone() throws Exception {
        when(achievementService.getAchievementsByProfileId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/achievements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAchievementById_ShouldReturnDTO() throws Exception {
        when(achievementService.getAchievementById(1L)).thenReturn(testDTO);

        mockMvc.perform(get("/api/v1/achievements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Coffee Consumed"));
    }

    @Test
    void getAchievementById_ShouldReturn404_WhenNotFound() throws Exception {
        when(achievementService.getAchievementById(999L))
                .thenThrow(new GenericException("Achievement not found with id: 999", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/achievements/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAchievement_ShouldReturnCreated() throws Exception {
        when(achievementService.addAchievement(any(AchievementDTO.class))).thenReturn(testDTO);

        mockMvc.perform(post("/api/v1/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Coffee Consumed"));
    }

    @Test
    void updateAchievement_ShouldReturnUpdated() throws Exception {
        when(achievementService.updateAchievement(eq(1L), any(AchievementDTO.class))).thenReturn(testDTO);

        mockMvc.perform(put("/api/v1/achievements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Coffee Consumed"));
    }

    @Test
    void deleteAchievement_ShouldReturnTrue() throws Exception {
        when(achievementService.deleteAchievement(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/achievements/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteAchievement_ShouldReturn404_WhenNotFound() throws Exception {
        when(achievementService.deleteAchievement(999L))
                .thenThrow(new GenericException("Achievement not found with id: 999", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/v1/achievements/999"))
                .andExpect(status().isNotFound());
    }

    // ── Security: unauthenticated access tests ──────────────────────────

    @Test
    @WithAnonymousUser
    void getAchievements_ShouldBeAccessible_WhenUnauthenticated() throws Exception {
        when(achievementService.getAchievementsByProfileId(1L)).thenReturn(List.of(testDTO));

        mockMvc.perform(get("/api/v1/achievements").param("profileId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void addAchievement_ShouldReturn401_WhenUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isUnauthorized());

        verify(achievementService, never()).addAchievement(any(AchievementDTO.class));
    }

    @Test
    @WithAnonymousUser
    void updateAchievement_ShouldReturn401_WhenUnauthenticated() throws Exception {
        mockMvc.perform(put("/api/v1/achievements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isUnauthorized());

        verify(achievementService, never()).updateAchievement(anyLong(), any(AchievementDTO.class));
    }

    @Test
    @WithAnonymousUser
    void deleteAchievement_ShouldReturn401_WhenUnauthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/achievements/1"))
                .andExpect(status().isUnauthorized());

        verify(achievementService, never()).deleteAchievement(anyLong());
    }
}
