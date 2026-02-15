package dev.bored.profile.service;

import dev.bored.profile.dto.AchievementDTO;
import dev.bored.profile.entity.Achievement;
import dev.bored.profile.entity.Profile;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.mapper.AchievementMapper;
import dev.bored.profile.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private AchievementService achievementService;

    private Achievement testEntity;
    private AchievementDTO testDTO;

    @BeforeEach
    void setUp() {
        Profile p = new Profile();
        p.setProfileId(1L);

        testEntity = new Achievement();
        testEntity.setAchievementId(1L);
        testEntity.setProfile(p);
        testEntity.setSlug("coffee");
        testEntity.setTitle("Coffee Consumed");
        testEntity.setProgressPercent(95);
        testEntity.setVariant("warning");
        testEntity.setSortOrder(1);

        testDTO = AchievementDTO.builder()
                .achievementId(1L).profileId(1L).id("coffee")
                .title("Coffee Consumed").progressPercent(95).variant("warning").sortOrder(1)
                .build();
    }

    @Test
    void getAchievementsByProfileId_ShouldReturnList() {
        when(achievementRepository.findByProfile_ProfileIdOrderBySortOrderAsc(1L)).thenReturn(List.of(testEntity));
        when(achievementMapper.toDTOList(List.of(testEntity))).thenReturn(List.of(testDTO));

        assertEquals(1, achievementService.getAchievementsByProfileId(1L).size());
    }

    @Test
    void getAchievementsByProfileId_ShouldReturnEmpty_WhenNone() {
        when(achievementRepository.findByProfile_ProfileIdOrderBySortOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(achievementMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(achievementService.getAchievementsByProfileId(1L).isEmpty());
    }

    @Test
    void getAchievementById_ShouldReturnDTO() {
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(achievementMapper.toDTO(testEntity)).thenReturn(testDTO);

        assertEquals("Coffee Consumed", achievementService.getAchievementById(1L).getTitle());
    }

    @Test
    void getAchievementById_ShouldThrow_WhenNotFound() {
        when(achievementRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(GenericException.class, () -> achievementService.getAchievementById(999L));
    }

    @Test
    void addAchievement_ShouldReturnSavedDTO() {
        when(achievementMapper.toEntity(testDTO)).thenReturn(testEntity);
        when(achievementRepository.save(testEntity)).thenReturn(testEntity);
        when(achievementMapper.toDTO(testEntity)).thenReturn(testDTO);

        assertEquals("Coffee Consumed", achievementService.addAchievement(testDTO).getTitle());
    }

    @Test
    void updateAchievement_ShouldReturnUpdatedDTO() {
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(achievementRepository.save(testEntity)).thenReturn(testEntity);
        when(achievementMapper.toDTO(testEntity)).thenReturn(testDTO);

        assertEquals("Coffee Consumed", achievementService.updateAchievement(1L, testDTO).getTitle());
    }

    @Test
    void updateAchievement_ShouldThrow_WhenNotFound() {
        when(achievementRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(GenericException.class, () -> achievementService.updateAchievement(999L, testDTO));
        verify(achievementRepository, never()).save(any());
    }

    @Test
    void deleteAchievement_ShouldReturnTrue() {
        when(achievementRepository.existsById(1L)).thenReturn(true);
        assertTrue(achievementService.deleteAchievement(1L));
        verify(achievementRepository).deleteById(1L);
    }

    @Test
    void deleteAchievement_ShouldThrow_WhenNotFound() {
        when(achievementRepository.existsById(999L)).thenReturn(false);
        assertThrows(GenericException.class, () -> achievementService.deleteAchievement(999L));
    }
}
