package dev.bored.profile.service;

import dev.bored.profile.dto.AchievementDTO;
import dev.bored.profile.entity.Achievement;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.mapper.AchievementMapper;
import dev.bored.profile.repository.AchievementRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing {@link Achievement} entities.
 * <p>
 * Provides CRUD operations for achievements associated with a profile,
 * including retrieval by profile, individual lookup, creation, update, and deletion.
 * All mutating operations are executed within a transactional context.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Service
@AllArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    /**
     * Retrieves all achievements associated with a given profile, ordered by sort order ascending.
     *
     * @param profileId the unique identifier of the profile whose achievements are requested
     * @return a list of {@link AchievementDTO} instances for the specified profile
     */
    @Transactional(readOnly = true)
    public List<AchievementDTO> getAchievementsByProfileId(Long profileId) {
        return achievementMapper.toDTOList(
                achievementRepository.findByProfile_ProfileIdOrderBySortOrderAsc(profileId));
    }

    /**
     * Retrieves a single achievement by its unique identifier.
     *
     * @param achievementId the unique identifier of the achievement to retrieve
     * @return the {@link AchievementDTO} corresponding to the given id
     * @throws GenericException if no achievement exists with the specified id (HTTP 404)
     */
    @Transactional(readOnly = true)
    public AchievementDTO getAchievementById(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new GenericException("Achievement not found with id: " + achievementId, HttpStatus.NOT_FOUND));
        return achievementMapper.toDTO(achievement);
    }

    /**
     * Creates a new achievement record.
     *
     * @param dto the data transfer object containing achievement details
     * @return the newly created {@link AchievementDTO} with its persisted state
     */
    @Transactional
    public AchievementDTO addAchievement(AchievementDTO dto) {
        Achievement entity = achievementMapper.toEntity(dto);
        return achievementMapper.toDTO(achievementRepository.save(entity));
    }

    /**
     * Updates an existing achievement with the supplied data.
     *
     * @param achievementId the unique identifier of the achievement to update
     * @param dto           the data transfer object containing updated achievement details
     * @return the updated {@link AchievementDTO}
     * @throws GenericException if no achievement exists with the specified id (HTTP 404)
     */
    @Transactional
    public AchievementDTO updateAchievement(Long achievementId, AchievementDTO dto) {
        Achievement existing = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new GenericException("Achievement not found with id: " + achievementId, HttpStatus.NOT_FOUND));

        existing.setSlug(dto.getId());
        existing.setTitle(dto.getTitle());
        existing.setSubtitle(dto.getSubtitle());
        existing.setEmoji(dto.getEmoji());
        existing.setProgressPercent(dto.getProgressPercent());
        existing.setVariant(dto.getVariant());
        existing.setStatLabel(dto.getStatLabel());
        existing.setStatValue(dto.getStatValue());
        existing.setSortOrder(dto.getSortOrder());

        return achievementMapper.toDTO(achievementRepository.save(existing));
    }

    /**
     * Deletes an achievement by its unique identifier.
     *
     * @param achievementId the unique identifier of the achievement to delete
     * @return {@code true} if the achievement was successfully deleted
     * @throws GenericException if no achievement exists with the specified id (HTTP 404)
     */
    @Transactional
    public boolean deleteAchievement(Long achievementId) {
        if (achievementRepository.existsById(achievementId)) {
            achievementRepository.deleteById(achievementId);
            return true;
        }
        throw new GenericException("Achievement not found with id: " + achievementId, HttpStatus.NOT_FOUND);
    }
}
