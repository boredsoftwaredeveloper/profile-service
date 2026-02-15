package dev.bored.profile.service;

import dev.bored.profile.dto.AspirationDTO;
import dev.bored.profile.entity.Aspiration;
import dev.bored.common.exception.GenericException;
import dev.bored.profile.mapper.AspirationMapper;
import dev.bored.profile.repository.AspirationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing {@link Aspiration} entities.
 * <p>
 * Provides CRUD operations for aspirations associated with a profile,
 * including retrieval by profile, individual lookup, creation, update, and deletion.
 * All mutating operations are executed within a transactional context.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Service
@AllArgsConstructor
public class AspirationService {

    private final AspirationRepository aspirationRepository;
    private final AspirationMapper aspirationMapper;

    /**
     * Retrieves all aspirations associated with a given profile, ordered by sort order ascending.
     *
     * @param profileId the unique identifier of the profile whose aspirations are requested
     * @return a list of {@link AspirationDTO} instances for the specified profile
     */
    @Transactional(readOnly = true)
    public List<AspirationDTO> getAspirationsByProfileId(Long profileId) {
        return aspirationMapper.toDTOList(
                aspirationRepository.findByProfile_ProfileIdOrderBySortOrderAsc(profileId));
    }

    /**
     * Retrieves a single aspiration by its unique identifier.
     *
     * @param aspirationId the unique identifier of the aspiration to retrieve
     * @return the {@link AspirationDTO} corresponding to the given id
     * @throws GenericException if no aspiration exists with the specified id (HTTP 404)
     */
    @Transactional(readOnly = true)
    public AspirationDTO getAspirationById(Long aspirationId) {
        Aspiration aspiration = aspirationRepository.findById(aspirationId)
                .orElseThrow(() -> new GenericException("Aspiration not found with id: " + aspirationId, HttpStatus.NOT_FOUND));
        return aspirationMapper.toDTO(aspiration);
    }

    /**
     * Creates a new aspiration record.
     *
     * @param dto the data transfer object containing aspiration details
     * @return the newly created {@link AspirationDTO} with its persisted state
     */
    @Transactional
    public AspirationDTO addAspiration(AspirationDTO dto) {
        Aspiration entity = aspirationMapper.toEntity(dto);
        return aspirationMapper.toDTO(aspirationRepository.save(entity));
    }

    /**
     * Updates an existing aspiration with the supplied data.
     *
     * @param aspirationId the unique identifier of the aspiration to update
     * @param dto          the data transfer object containing updated aspiration details
     * @return the updated {@link AspirationDTO}
     * @throws GenericException if no aspiration exists with the specified id (HTTP 404)
     */
    @Transactional
    public AspirationDTO updateAspiration(Long aspirationId, AspirationDTO dto) {
        Aspiration existing = aspirationRepository.findById(aspirationId)
                .orElseThrow(() -> new GenericException("Aspiration not found with id: " + aspirationId, HttpStatus.NOT_FOUND));

        existing.setSlug(dto.getId());
        existing.setTitle(dto.getTitle());
        existing.setSubtitle(dto.getSubtitle());
        existing.setStatusText(dto.getStatusText());
        existing.setProgressPercent(dto.getProgressPercent());
        existing.setVariant(dto.getVariant());
        existing.setFooterText(dto.getFooterText());
        existing.setAnimated(dto.getAnimated());
        existing.setSortOrder(dto.getSortOrder());

        return aspirationMapper.toDTO(aspirationRepository.save(existing));
    }

    /**
     * Deletes an aspiration by its unique identifier.
     *
     * @param aspirationId the unique identifier of the aspiration to delete
     * @return {@code true} if the aspiration was successfully deleted
     * @throws GenericException if no aspiration exists with the specified id (HTTP 404)
     */
    @Transactional
    public boolean deleteAspiration(Long aspirationId) {
        if (aspirationRepository.existsById(aspirationId)) {
            aspirationRepository.deleteById(aspirationId);
            return true;
        }
        throw new GenericException("Aspiration not found with id: " + aspirationId, HttpStatus.NOT_FOUND);
    }
}
