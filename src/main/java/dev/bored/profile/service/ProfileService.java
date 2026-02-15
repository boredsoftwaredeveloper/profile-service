package dev.bored.profile.service;

import dev.bored.profile.dto.ProfileDTO;
import dev.bored.profile.entity.Profile;
import dev.bored.profile.exception.GenericException;
import dev.bored.profile.mapper.ProfileMapper;
import dev.bored.profile.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing {@link Profile} entities.
 * <p>
 * Provides CRUD operations for user profiles, including retrieval,
 * creation, update, and deletion. All mutating operations are
 * executed within a transactional context.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    /**
     * Retrieves a profile by its unique identifier.
     *
     * @param profileId the unique identifier of the profile to retrieve
     * @return the {@link ProfileDTO} corresponding to the given id
     * @throws GenericException if no profile exists with the specified id (HTTP 404)
     */
    @Transactional(readOnly = true)
    public ProfileDTO getProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new GenericException("Profile not found with id: " + profileId, HttpStatus.NOT_FOUND));
        return profileMapper.toDTO(profile);
    }

    /**
     * Creates a new profile.
     *
     * @param profileDTO the data transfer object containing profile details
     * @return the newly created {@link ProfileDTO} with its persisted state
     */
    @Transactional
    public ProfileDTO addProfile(ProfileDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDTO(savedProfile);
    }

    /**
     * Updates an existing profile with the supplied data.
     *
     * @param profileId  the unique identifier of the profile to update
     * @param profileDTO the data transfer object containing updated profile details
     * @return the updated {@link ProfileDTO}
     * @throws GenericException if no profile exists with the specified id (HTTP 404)
     */
    @Transactional
    public ProfileDTO updateProfile(Long profileId, ProfileDTO profileDTO) {
        Profile existing = profileRepository.findById(profileId)
                .orElseThrow(() -> new GenericException("Profile not found with id: " + profileId, HttpStatus.NOT_FOUND));

        existing.setFirstName(profileDTO.getFirstName());
        existing.setLastName(profileDTO.getLastName());
        existing.setPhotoUrl(profileDTO.getPhotoUrl());
        existing.setStatus(profileDTO.getStatus());

        Profile updated = profileRepository.save(existing);
        return profileMapper.toDTO(updated);
    }

    /**
     * Deletes a profile by its unique identifier.
     *
     * @param profileId the unique identifier of the profile to delete
     * @return {@code true} if the profile was successfully deleted
     * @throws GenericException if no profile exists with the specified id (HTTP 404)
     */
    @Transactional
    public boolean deleteProfile(Long profileId) {
        if (profileRepository.existsById(profileId)) {
            profileRepository.deleteById(profileId);
            return true;
        } else {
            throw new GenericException("Profile not found with id: " + profileId, HttpStatus.NOT_FOUND);
        }
    }
}