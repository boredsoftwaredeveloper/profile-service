package dev.bored.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bored.profile.dto.ProfileDTO;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.config.SecurityConfig;
import dev.bored.profile.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link ProfileController}.
 * <p>
 * This test class uses {@link WebMvcTest} to test the controller layer in isolation,
 * with the {@link ProfileService} being mocked using Mockito.
 * </p>
 *
 * @author Bored Software Developer
 * @version 1.0
 * @since 2026-01-21
 */
@WebMvcTest(ProfileController.class)
@Import(SecurityConfig.class)
@WithMockUser
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProfileService profileService;

    private ProfileDTO testProfileDTO;

    /**
     * Sets up test data before each test execution.
     * <p>
     * Initializes a sample {@link ProfileDTO} object with test data
     * that can be reused across multiple test methods.
     * </p>
     */
    @BeforeEach
    void setUp() {
        testProfileDTO = ProfileDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .photoUrl("https://example.com/photo.jpg")
                .status("Bored but coding")
                .build();
    }

    /**
     * Tests the successful retrieval of a profile by ID.
     * <p>
     * Verifies that when a valid profile ID is provided, the controller
     * returns HTTP 200 OK status with the correct profile data in JSON format.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void getProfileById_ShouldReturnProfile_WhenProfileExists() throws Exception {
        // Arrange
        Long profileId = 1L;
        when(profileService.getProfileById(profileId)).thenReturn(testProfileDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/profiles/{profileId}", profileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.photoUrl").value("https://example.com/photo.jpg"))
                .andExpect(jsonPath("$.status").value("Bored but coding"));

        verify(profileService, times(1)).getProfileById(profileId);
    }

    /**
     * Tests the error handling when retrieving a non-existent profile.
     * <p>
     * Verifies that when a profile ID does not exist, the controller
     * returns HTTP 404 Not Found status.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void getProfileById_ShouldReturnError_WhenProfileNotFound() throws Exception {
        // Arrange
        Long profileId = 999L;
        when(profileService.getProfileById(profileId))
                .thenThrow(new GenericException("Profile not found with id: " + profileId, org.springframework.http.HttpStatus.NOT_FOUND));

        // Act & Assert
        mockMvc.perform(get("/api/v1/profiles/{profileId}", profileId))
                .andExpect(status().isNotFound());

        verify(profileService, times(1)).getProfileById(profileId);
    }

    /**
     * Tests the successful creation of a new profile.
     * <p>
     * Verifies that when valid profile data is provided, the controller
     * returns HTTP 200 OK status with the created profile data in JSON format.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void addProfile_ShouldReturnCreatedProfile_WhenValidInput() throws Exception {
        // Arrange
        ProfileDTO inputDTO = ProfileDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .photoUrl("https://example.com/jane.jpg")
                .status("Active")
                .build();

        ProfileDTO savedDTO = ProfileDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .photoUrl("https://example.com/jane.jpg")
                .status("Active")
                .build();

        when(profileService.addProfile(any(ProfileDTO.class))).thenReturn(savedDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.photoUrl").value("https://example.com/jane.jpg"))
                .andExpect(jsonPath("$.status").value("Active"));

        verify(profileService, times(1)).addProfile(any(ProfileDTO.class));
    }

    /**
     * Tests the successful update of an existing profile.
     * <p>
     * Verifies that when a valid profile ID and updated data are provided,
     * the controller returns HTTP 200 OK status with the updated profile data in JSON format.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void updateProfile_ShouldReturnUpdatedProfile_WhenProfileExists() throws Exception {
        // Arrange
        Long profileId = 1L;
        ProfileDTO updatedDTO = ProfileDTO.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .photoUrl("https://example.com/updated.jpg")
                .status("Updated")
                .build();

        when(profileService.updateProfile(eq(profileId), any(ProfileDTO.class)))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/profiles/{profileId}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John Updated"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                .andExpect(jsonPath("$.photoUrl").value("https://example.com/updated.jpg"))
                .andExpect(jsonPath("$.status").value("Updated"));

        verify(profileService, times(1)).updateProfile(eq(profileId), any(ProfileDTO.class));
    }

    /**
     * Tests the error handling when updating a non-existent profile.
     * <p>
     * Verifies that when a profile ID does not exist during an update operation,
     * the controller returns HTTP 500 Internal Server Error status.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void updateProfile_ShouldReturnError_WhenProfileNotFound() throws Exception {
        // Arrange
        Long profileId = 999L;
        ProfileDTO updatedDTO = ProfileDTO.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .build();

        when(profileService.updateProfile(eq(profileId), any(ProfileDTO.class)))
                .thenThrow(new GenericException("Profile not found with id: " + profileId, org.springframework.http.HttpStatus.NOT_FOUND));

        // Act & Assert
        mockMvc.perform(put("/api/v1/profiles/{profileId}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isNotFound());

        verify(profileService, times(1)).updateProfile(eq(profileId), any(ProfileDTO.class));
    }

    /**
     * Tests the successful deletion of an existing profile.
     * <p>
     * Verifies that when a valid profile ID is provided for deletion,
     * the controller returns HTTP 200 OK status with a boolean value of true.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void deleteProfile_ShouldReturnTrue_WhenProfileExists() throws Exception {
        // Arrange
        Long profileId = 1L;
        when(profileService.deleteProfile(profileId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/profiles")
                        .param("profileId", profileId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(profileService, times(1)).deleteProfile(profileId);
    }

    /**
     * Tests the deletion attempt of a non-existent profile.
     * <p>
     * Verifies that when a profile ID does not exist during a delete operation,
     * the controller returns HTTP 404 Not Found status.
     * </p>
     *
     * @throws Exception if the mock MVC request fails
     */
    @Test
    void deleteProfile_ShouldReturn404_WhenProfileNotFound() throws Exception {
        // Arrange
        Long profileId = 999L;
        when(profileService.deleteProfile(profileId))
                .thenThrow(new GenericException("Profile not found with id: " + profileId, org.springframework.http.HttpStatus.NOT_FOUND));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/profiles")
                        .param("profileId", profileId.toString()))
                .andExpect(status().isNotFound());

        verify(profileService, times(1)).deleteProfile(profileId);
    }
}