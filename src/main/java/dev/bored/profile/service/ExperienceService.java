package dev.bored.profile.service;

import dev.bored.profile.dto.ExperienceDTO;
import dev.bored.profile.entity.Experience;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.mapper.ExperienceMapper;
import dev.bored.profile.repository.ExperienceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing {@link Experience} entities.
 * <p>
 * Provides CRUD operations for work experiences associated with a profile,
 * including retrieval by profile, individual lookup, creation, update, and deletion.
 * All mutating operations are executed within a transactional context.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Service
@AllArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    /**
     * Retrieves all experiences associated with a given profile, ordered by sort order ascending.
     *
     * @param profileId the unique identifier of the profile whose experiences are requested
     * @return a list of {@link ExperienceDTO} instances for the specified profile
     */
    @Transactional(readOnly = true)
    public List<ExperienceDTO> getExperiencesByProfileId(Long profileId) {
        return experienceMapper.toDTOList(
                experienceRepository.findByProfile_ProfileIdOrderBySortOrderAsc(profileId));
    }

    /**
     * Retrieves a single experience by its unique identifier.
     *
     * @param experienceId the unique identifier of the experience to retrieve
     * @return the {@link ExperienceDTO} corresponding to the given id
     * @throws GenericException if no experience exists with the specified id (HTTP 404)
     */
    @Transactional(readOnly = true)
    public ExperienceDTO getExperienceById(Long experienceId) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new GenericException("Experience not found with id: " + experienceId, HttpStatus.NOT_FOUND));
        return experienceMapper.toDTO(experience);
    }

    /**
     * Creates a new experience record.
     *
     * @param dto the data transfer object containing experience details
     * @return the newly created {@link ExperienceDTO} with its persisted state
     */
    @Transactional
    public ExperienceDTO addExperience(ExperienceDTO dto) {
        Experience entity = experienceMapper.toEntity(dto);
        return experienceMapper.toDTO(experienceRepository.save(entity));
    }

    /**
     * Updates an existing experience with the supplied data.
     *
     * @param experienceId the unique identifier of the experience to update
     * @param dto          the data transfer object containing updated experience details
     * @return the updated {@link ExperienceDTO}
     * @throws GenericException if no experience exists with the specified id (HTTP 404)
     */
    @Transactional
    public ExperienceDTO updateExperience(Long experienceId, ExperienceDTO dto) {
        Experience existing = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new GenericException("Experience not found with id: " + experienceId, HttpStatus.NOT_FOUND));

        existing.setSlug(dto.getId());
        existing.setCompany(dto.getCompany());
        existing.setRole(dto.getRole());
        existing.setRoleStyle(dto.getRoleStyle());
        existing.setDescription(dto.getDescription());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setSortOrder(dto.getSortOrder());

        return experienceMapper.toDTO(experienceRepository.save(existing));
    }

    /**
     * Deletes an experience by its unique identifier.
     *
     * @param experienceId the unique identifier of the experience to delete
     * @return {@code true} if the experience was successfully deleted
     * @throws GenericException if no experience exists with the specified id (HTTP 404)
     */
    @Transactional
    public boolean deleteExperience(Long experienceId) {
        if (experienceRepository.existsById(experienceId)) {
            experienceRepository.deleteById(experienceId);
            return true;
        }
        throw new GenericException("Experience not found with id: " + experienceId, HttpStatus.NOT_FOUND);
    }
}
