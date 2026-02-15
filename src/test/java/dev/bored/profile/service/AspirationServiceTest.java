package dev.bored.profile.service;

import dev.bored.profile.dto.AspirationDTO;
import dev.bored.profile.entity.Aspiration;
import dev.bored.profile.entity.Profile;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.mapper.AspirationMapper;
import dev.bored.profile.repository.AspirationRepository;
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
class AspirationServiceTest {

    @Mock
    private AspirationRepository aspirationRepository;

    @Mock
    private AspirationMapper aspirationMapper;

    @InjectMocks
    private AspirationService aspirationService;

    private Aspiration testEntity;
    private AspirationDTO testDTO;

    @BeforeEach
    void setUp() {
        Profile p = new Profile();
        p.setProfileId(1L);

        testEntity = new Aspiration();
        testEntity.setAspirationId(1L);
        testEntity.setProfile(p);
        testEntity.setSlug("cloud-arch");
        testEntity.setTitle("Cloud Architecture");
        testEntity.setProgressPercent(60);
        testEntity.setVariant("info");
        testEntity.setAnimated(true);
        testEntity.setSortOrder(1);

        testDTO = AspirationDTO.builder()
                .aspirationId(1L).profileId(1L).id("cloud-arch")
                .title("Cloud Architecture").progressPercent(60).variant("info").animated(true).sortOrder(1)
                .build();
    }

    @Test
    void getAspirationsByProfileId_ShouldReturnList() {
        when(aspirationRepository.findByProfile_ProfileIdOrderBySortOrderAsc(1L)).thenReturn(List.of(testEntity));
        when(aspirationMapper.toDTOList(List.of(testEntity))).thenReturn(List.of(testDTO));

        assertEquals(1, aspirationService.getAspirationsByProfileId(1L).size());
    }

    @Test
    void getAspirationsByProfileId_ShouldReturnEmpty_WhenNone() {
        when(aspirationRepository.findByProfile_ProfileIdOrderBySortOrderAsc(1L)).thenReturn(Collections.emptyList());
        when(aspirationMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        assertTrue(aspirationService.getAspirationsByProfileId(1L).isEmpty());
    }

    @Test
    void getAspirationById_ShouldReturnDTO() {
        when(aspirationRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(aspirationMapper.toDTO(testEntity)).thenReturn(testDTO);

        assertEquals("Cloud Architecture", aspirationService.getAspirationById(1L).getTitle());
    }

    @Test
    void getAspirationById_ShouldThrow_WhenNotFound() {
        when(aspirationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(GenericException.class, () -> aspirationService.getAspirationById(999L));
    }

    @Test
    void addAspiration_ShouldReturnSavedDTO() {
        when(aspirationMapper.toEntity(testDTO)).thenReturn(testEntity);
        when(aspirationRepository.save(testEntity)).thenReturn(testEntity);
        when(aspirationMapper.toDTO(testEntity)).thenReturn(testDTO);

        assertEquals("Cloud Architecture", aspirationService.addAspiration(testDTO).getTitle());
    }

    @Test
    void updateAspiration_ShouldReturnUpdatedDTO() {
        when(aspirationRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(aspirationRepository.save(testEntity)).thenReturn(testEntity);
        when(aspirationMapper.toDTO(testEntity)).thenReturn(testDTO);

        assertEquals("Cloud Architecture", aspirationService.updateAspiration(1L, testDTO).getTitle());
    }

    @Test
    void updateAspiration_ShouldThrow_WhenNotFound() {
        when(aspirationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(GenericException.class, () -> aspirationService.updateAspiration(999L, testDTO));
        verify(aspirationRepository, never()).save(any());
    }

    @Test
    void deleteAspiration_ShouldReturnTrue() {
        when(aspirationRepository.existsById(1L)).thenReturn(true);
        assertTrue(aspirationService.deleteAspiration(1L));
        verify(aspirationRepository).deleteById(1L);
    }

    @Test
    void deleteAspiration_ShouldThrow_WhenNotFound() {
        when(aspirationRepository.existsById(999L)).thenReturn(false);
        assertThrows(GenericException.class, () -> aspirationService.deleteAspiration(999L));
    }
}
