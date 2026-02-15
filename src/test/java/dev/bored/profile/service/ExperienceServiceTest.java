package dev.bored.profile.service;

import dev.bored.profile.dto.ExperienceDTO;
import dev.bored.profile.entity.Experience;
import dev.bored.profile.entity.Profile;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.mapper.ExperienceMapper;
import dev.bored.profile.repository.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private ExperienceMapper experienceMapper;

    @InjectMocks
    private ExperienceService experienceService;

    private Profile testProfile;
    private Experience testExperience;
    private ExperienceDTO testDTO;

    @BeforeEach
    void setUp() {
        testProfile = new Profile();
        testProfile.setProfileId(1L);

        testExperience = new Experience();
        testExperience.setExperienceId(1L);
        testExperience.setProfile(testProfile);
        testExperience.setSlug("googol");
        testExperience.setCompany("Googol");
        testExperience.setRole("Senior Dev");
        testExperience.setRoleStyle("frontend");
        testExperience.setStartDate(LocalDate.of(2023, 1, 15));
        testExperience.setEndDate(LocalDate.of(2025, 6, 30));
        testExperience.setSortOrder(1);

        testDTO = ExperienceDTO.builder()
                .experienceId(1L).profileId(1L).id("googol")
                .company("Googol").role("Senior Dev").roleStyle("frontend")
                .startDate(LocalDate.of(2023, 1, 15))
                .endDate(LocalDate.of(2025, 6, 30))
                .sortOrder(1)
                .build();
    }

    @Test
    void getExperiencesByProfileId_ShouldReturnList() {
        when(experienceRepository.findByProfile_ProfileIdOrderBySortOrderAsc(1L)).thenReturn(List.of(testExperience));
        when(experienceMapper.toDTOList(List.of(testExperience))).thenReturn(List.of(testDTO));

        List<ExperienceDTO> result = experienceService.getExperiencesByProfileId(1L);

        assertEquals(1, result.size());
        assertEquals("Googol", result.get(0).getCompany());
    }

    @Test
    void getExperiencesByProfileId_ShouldReturnEmpty_WhenNone() {
        when(experienceRepository.findByProfile_ProfileIdOrderBySortOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(experienceMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(experienceService.getExperiencesByProfileId(1L).isEmpty());
    }

    @Test
    void getExperienceById_ShouldReturnDTO() {
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));
        when(experienceMapper.toDTO(testExperience)).thenReturn(testDTO);

        ExperienceDTO result = experienceService.getExperienceById(1L);

        assertEquals("Googol", result.getCompany());
    }

    @Test
    void getExperienceById_ShouldThrow_WhenNotFound() {
        when(experienceRepository.findById(999L)).thenReturn(Optional.empty());

        GenericException ex = assertThrows(GenericException.class, () -> experienceService.getExperienceById(999L));
        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void addExperience_ShouldReturnSavedDTO() {
        when(experienceMapper.toEntity(testDTO)).thenReturn(testExperience);
        when(experienceRepository.save(testExperience)).thenReturn(testExperience);
        when(experienceMapper.toDTO(testExperience)).thenReturn(testDTO);

        ExperienceDTO result = experienceService.addExperience(testDTO);

        assertEquals("Googol", result.getCompany());
        verify(experienceRepository).save(testExperience);
    }

    @Test
    void updateExperience_ShouldReturnUpdatedDTO() {
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));
        when(experienceRepository.save(testExperience)).thenReturn(testExperience);
        when(experienceMapper.toDTO(testExperience)).thenReturn(testDTO);

        ExperienceDTO result = experienceService.updateExperience(1L, testDTO);

        assertEquals("Googol", result.getCompany());
        verify(experienceRepository).save(testExperience);
    }

    @Test
    void updateExperience_ShouldThrow_WhenNotFound() {
        when(experienceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GenericException.class, () -> experienceService.updateExperience(999L, testDTO));
        verify(experienceRepository, never()).save(any());
    }

    @Test
    void deleteExperience_ShouldReturnTrue() {
        when(experienceRepository.existsById(1L)).thenReturn(true);

        assertTrue(experienceService.deleteExperience(1L));
        verify(experienceRepository).deleteById(1L);
    }

    @Test
    void deleteExperience_ShouldThrow_WhenNotFound() {
        when(experienceRepository.existsById(999L)).thenReturn(false);

        assertThrows(GenericException.class, () -> experienceService.deleteExperience(999L));
        verify(experienceRepository, never()).deleteById(any());
    }
}
