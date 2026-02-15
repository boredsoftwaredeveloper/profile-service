package dev.bored.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bored.profile.dto.AspirationDTO;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.config.SecurityConfig;
import dev.bored.profile.service.AspirationService;
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

@WebMvcTest(AspirationController.class)
@Import(SecurityConfig.class)
@WithMockUser
class AspirationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AspirationService aspirationService;

    private final AspirationDTO testDTO = AspirationDTO.builder()
            .aspirationId(1L).profileId(1L).id("cloud")
            .title("Cloud Architecture").progressPercent(60).variant("info").animated(true).sortOrder(1)
            .build();

    @Test
    void getAspirations_ShouldReturnList() throws Exception {
        when(aspirationService.getAspirationsByProfileId(1L)).thenReturn(List.of(testDTO));

        mockMvc.perform(get("/api/v1/aspirations").param("profileId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Cloud Architecture"));
    }

    @Test
    void getAspirations_ShouldReturnEmpty_WhenNone() throws Exception {
        when(aspirationService.getAspirationsByProfileId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/aspirations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAspirationById_ShouldReturnDTO() throws Exception {
        when(aspirationService.getAspirationById(1L)).thenReturn(testDTO);

        mockMvc.perform(get("/api/v1/aspirations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cloud Architecture"));
    }

    @Test
    void getAspirationById_ShouldReturn404_WhenNotFound() throws Exception {
        when(aspirationService.getAspirationById(999L))
                .thenThrow(new GenericException("Aspiration not found with id: 999", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/aspirations/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAspiration_ShouldReturnCreated() throws Exception {
        when(aspirationService.addAspiration(any(AspirationDTO.class))).thenReturn(testDTO);

        mockMvc.perform(post("/api/v1/aspirations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cloud Architecture"));
    }

    @Test
    void updateAspiration_ShouldReturnUpdated() throws Exception {
        when(aspirationService.updateAspiration(eq(1L), any(AspirationDTO.class))).thenReturn(testDTO);

        mockMvc.perform(put("/api/v1/aspirations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cloud Architecture"));
    }

    @Test
    void deleteAspiration_ShouldReturnTrue() throws Exception {
        when(aspirationService.deleteAspiration(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/aspirations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteAspiration_ShouldReturn404_WhenNotFound() throws Exception {
        when(aspirationService.deleteAspiration(999L))
                .thenThrow(new GenericException("Aspiration not found with id: 999", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/v1/aspirations/999"))
                .andExpect(status().isNotFound());
    }

    // ── Security: unauthenticated access tests ──────────────────────────

    @Test
    @WithAnonymousUser
    void getAspirations_ShouldBeAccessible_WhenUnauthenticated() throws Exception {
        when(aspirationService.getAspirationsByProfileId(1L)).thenReturn(List.of(testDTO));

        mockMvc.perform(get("/api/v1/aspirations").param("profileId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void addAspiration_ShouldReturn401_WhenUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/aspirations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isUnauthorized());

        verify(aspirationService, never()).addAspiration(any(AspirationDTO.class));
    }

    @Test
    @WithAnonymousUser
    void updateAspiration_ShouldReturn401_WhenUnauthenticated() throws Exception {
        mockMvc.perform(put("/api/v1/aspirations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isUnauthorized());

        verify(aspirationService, never()).updateAspiration(anyLong(), any(AspirationDTO.class));
    }

    @Test
    @WithAnonymousUser
    void deleteAspiration_ShouldReturn401_WhenUnauthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/aspirations/1"))
                .andExpect(status().isUnauthorized());

        verify(aspirationService, never()).deleteAspiration(anyLong());
    }
}
