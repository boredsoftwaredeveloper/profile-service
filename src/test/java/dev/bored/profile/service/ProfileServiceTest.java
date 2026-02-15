package dev.bored.profile.service;

import dev.bored.profile.dto.ProfileDTO;
import dev.bored.profile.entity.Profile;
import dev.bored.profile.exception.GenericException;
import dev.bored.profile.mapper.ProfileMapper;
import dev.bored.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProfileService}.
 * <p>
 * This test class uses Mockito to mock dependencies and test the business logic
 * of the ProfileService in isolation. All repository and mapper interactions are mocked.
 * </p>
 *
 * @author Bored Software Developer
 * @version 1.0
 * @since 2026-01-21
 */
@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    private Profile testProfile;
    private ProfileDTO testProfileDTO;

    /**
     * Sets up test data before each test execution.
     * <p>
     * Initializes sample {@link Profile} entity and {@link ProfileDTO} objects
     * with test data that can be reused across multiple test methods.
     * </p>
     */
    @BeforeEach
    void setUp() {
        testProfile = new Profile();
        testProfile.setProfileId(1L);
        testProfile.setFirstName("John");
        testProfile.setLastName("Doe");
        testProfile.setPhotoUrl("https://example.com/photo.jpg");
        testProfile.setStatus("Bored but coding");

        testProfileDTO = ProfileDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .photoUrl("https://example.com/photo.jpg")
                .status("Bored but coding")
                .build();
    }

    /**
     * Tests successful retrieval of a profile by ID.
     * <p>
     * Verifies that when a valid profile ID is provided and the profile exists,
     * the service correctly retrieves the profile from the repository,
     * maps it to a DTO, and returns the result.
     * </p>
     */
    @Test
    void getProfileById_ShouldReturnProfileDTO_WhenProfileExists() {
        // Arrange
        Long profileId = 1L;
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(testProfile));
        when(profileMapper.toDTO(testProfile)).thenReturn(testProfileDTO);

        // Act
        ProfileDTO result = profileService.getProfileById(profileId);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("https://example.com/photo.jpg", result.getPhotoUrl());
        assertEquals("Bored but coding", result.getStatus());
        verify(profileRepository, times(1)).findById(profileId);
        verify(profileMapper, times(1)).toDTO(testProfile);
    }

    /**
     * Tests retrieval of a non-existent profile by ID.
     * <p>
     * Verifies that when a profile ID does not exist in the repository,
     * the service throws a {@link GenericException} with the appropriate error message.
     * </p>
     */
    @Test
    void getProfileById_ShouldThrowException_WhenProfileNotFound() {
        // Arrange
        Long profileId = 999L;
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // Act & Assert
        GenericException exception = assertThrows(GenericException.class, () -> profileService.getProfileById(profileId));

        assertEquals("Profile not found with id: 999", exception.getMessage());
        verify(profileRepository, times(1)).findById(profileId);
        verify(profileMapper, never()).toDTO(any());
    }

    /**
     * Tests successful creation of a new profile.
     * <p>
     * Verifies that when a valid {@link ProfileDTO} is provided,
     * the service correctly converts it to an entity, saves it to the repository,
     * and returns the saved profile as a DTO.
     * </p>
     */
    @Test
    void addProfile_ShouldReturnSavedProfileDTO_WhenValidInput() {
        // Arrange
        ProfileDTO inputDTO = ProfileDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .photoUrl("https://example.com/jane.jpg")
                .status("Active")
                .build();

        Profile profileToSave = new Profile();
        profileToSave.setFirstName("Jane");
        profileToSave.setLastName("Smith");
        profileToSave.setPhotoUrl("https://example.com/jane.jpg");
        profileToSave.setStatus("Active");

        Profile savedProfile = new Profile();
        savedProfile.setProfileId(2L);
        savedProfile.setFirstName("Jane");
        savedProfile.setLastName("Smith");
        savedProfile.setPhotoUrl("https://example.com/jane.jpg");
        savedProfile.setStatus("Active");

        ProfileDTO savedProfileDTO = ProfileDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .photoUrl("https://example.com/jane.jpg")
                .status("Active")
                .build();

        when(profileMapper.toEntity(inputDTO)).thenReturn(profileToSave);
        when(profileRepository.save(profileToSave)).thenReturn(savedProfile);
        when(profileMapper.toDTO(savedProfile)).thenReturn(savedProfileDTO);

        // Act
        ProfileDTO result = profileService.addProfile(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("https://example.com/jane.jpg", result.getPhotoUrl());
        assertEquals("Active", result.getStatus());
        verify(profileMapper, times(1)).toEntity(inputDTO);
        verify(profileRepository, times(1)).save(profileToSave);
        verify(profileMapper, times(1)).toDTO(savedProfile);
    }

    /**
     * Tests successful update of an existing profile.
     * <p>
     * Verifies that when a valid profile ID and updated data are provided,
     * the service retrieves the existing profile, updates its fields,
     * saves the changes, and returns the updated profile as a DTO.
     * </p>
     */
    @Test
    void updateProfile_ShouldReturnUpdatedProfileDTO_WhenProfileExists() {
        // Arrange
        Long profileId = 1L;
        ProfileDTO updatedDTO = ProfileDTO.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .photoUrl("https://example.com/updated.jpg")
                .status("Updated")
                .build();

        Profile existingProfile = new Profile();
        existingProfile.setProfileId(profileId);
        existingProfile.setFirstName("John");
        existingProfile.setLastName("Doe");
        existingProfile.setPhotoUrl("https://example.com/photo.jpg");
        existingProfile.setStatus("Bored but coding");

        Profile updatedProfile = new Profile();
        updatedProfile.setProfileId(profileId);
        updatedProfile.setFirstName("John Updated");
        updatedProfile.setLastName("Doe Updated");
        updatedProfile.setPhotoUrl("https://example.com/updated.jpg");
        updatedProfile.setStatus("Updated");

        ProfileDTO resultDTO = ProfileDTO.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .photoUrl("https://example.com/updated.jpg")
                .status("Updated")
                .build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(existingProfile)).thenReturn(updatedProfile);
        when(profileMapper.toDTO(updatedProfile)).thenReturn(resultDTO);

        // Act
        ProfileDTO result = profileService.updateProfile(profileId, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe Updated", result.getLastName());
        assertEquals("https://example.com/updated.jpg", result.getPhotoUrl());
        assertEquals("Updated", result.getStatus());
        verify(profileRepository, times(1)).findById(profileId);
        verify(profileRepository, times(1)).save(existingProfile);
        verify(profileMapper, times(1)).toDTO(updatedProfile);
    }

    /**
     * Tests update attempt on a non-existent profile.
     * <p>
     * Verifies that when attempting to update a profile that does not exist,
     * the service throws a {@link GenericException} with the appropriate error message.
     * </p>
     */
    @Test
    void updateProfile_ShouldThrowException_WhenProfileNotFound() {
        // Arrange
        Long profileId = 999L;
        ProfileDTO updatedDTO = ProfileDTO.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // Act & Assert
        GenericException exception = assertThrows(GenericException.class, () -> profileService.updateProfile(profileId, updatedDTO));

        assertEquals("Profile not found with id: 999", exception.getMessage());
        verify(profileRepository, times(1)).findById(profileId);
        verify(profileRepository, never()).save(any());
        verify(profileMapper, never()).toDTO(any());
    }

    /**
     * Tests successful deletion of an existing profile.
     * <p>
     * Verifies that when a valid profile ID is provided and the profile exists,
     * the service successfully deletes the profile from the repository
     * and returns true to indicate successful deletion.
     * </p>
     */
    @Test
    void deleteProfile_ShouldReturnTrue_WhenProfileExists() {
        // Arrange
        Long profileId = 1L;
        when(profileRepository.existsById(profileId)).thenReturn(true);
        doNothing().when(profileRepository).deleteById(profileId);

        // Act
        boolean result = profileService.deleteProfile(profileId);

        // Assert
        assertTrue(result);
        verify(profileRepository, times(1)).existsById(profileId);
        verify(profileRepository, times(1)).deleteById(profileId);
    }

    /**
     * Tests deletion attempt on a non-existent profile.
     * <p>
     * Verifies that when attempting to delete a profile that does not exist,
     * the service throws a {@link GenericException} with the appropriate error message
     * and does not attempt to delete from the repository.
     * </p>
     */
    @Test
    void deleteProfile_ShouldThrowException_WhenProfileNotFound() {
        // Arrange
        Long profileId = 999L;
        when(profileRepository.existsById(profileId)).thenReturn(false);

        // Act & Assert
        GenericException exception = assertThrows(GenericException.class, () -> profileService.deleteProfile(profileId));

        assertEquals("Profile not found with id: 999", exception.getMessage());
        verify(profileRepository, times(1)).existsById(profileId);
        verify(profileRepository, never()).deleteById(any());
    }

    /**
     * Tests that the mapper is never called when profile is not found during retrieval.
     * <p>
     * Verifies the service's defensive programming by ensuring that the mapper
     * is not invoked unnecessarily when a profile lookup fails.
     * </p>
     */
    @Test
    void getProfileById_ShouldNotCallMapper_WhenProfileNotFound() {
        // Arrange
        Long profileId = 999L;
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GenericException.class, () -> profileService.getProfileById(profileId));

        verify(profileMapper, never()).toDTO(any());
    }

    /**
     * Tests that updateProfile correctly modifies both firstName and lastName.
     * <p>
     * Verifies that the update operation properly sets both field values
     * on the existing profile entity before saving.
     * </p>
     */
    @Test
    void updateProfile_ShouldUpdateBothFields_WhenCalledWithNewData() {
        // Arrange
        Long profileId = 1L;
        ProfileDTO updatedDTO = ProfileDTO.builder()
                .firstName("NewFirst")
                .lastName("NewLast")
                .photoUrl("https://example.com/new.jpg")
                .status("New Status")
                .build();

        Profile existingProfile = new Profile();
        existingProfile.setProfileId(profileId);
        existingProfile.setFirstName("OldFirst");
        existingProfile.setLastName("OldLast");
        existingProfile.setPhotoUrl("https://example.com/old.jpg");
        existingProfile.setStatus("Old Status");

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(existingProfile);
        when(profileMapper.toDTO(any(Profile.class))).thenReturn(updatedDTO);

        // Act
        profileService.updateProfile(profileId, updatedDTO);

        // Assert
        assertEquals("NewFirst", existingProfile.getFirstName());
        assertEquals("NewLast", existingProfile.getLastName());
        assertEquals("https://example.com/new.jpg", existingProfile.getPhotoUrl());
        assertEquals("New Status", existingProfile.getStatus());
        verify(profileRepository, times(1)).save(existingProfile);
    }
}
