package dev.bored.profile.service;

import dev.bored.profile.dto.ProfileDTO;
import dev.bored.profile.entity.Profile;
import dev.bored.profile.mapper.ProfileMapper;
import dev.bored.profile.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Profile entities.
 */
@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    /**
     * Retrieves a Profile by its ID.
     *
     * @param profileId the ID of the profile to retrieve
     * @return the ProfileDTO corresponding to the given ID
     */
    @Transactional(readOnly = true)
    public ProfileDTO getProfileById(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return profileMapper.toDTO(profile);
    }

    /**
     * Saves a Profile.
     *
     * @param profileDTO the ProfileDTO to save
     * @return the saved ProfileDTO
     */
    @Transactional
    public ProfileDTO addProfile(ProfileDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDTO(savedProfile);
    }

    @Transactional
    public ProfileDTO updateProfile(Long profileId, ProfileDTO profileDTO) {
        Profile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        existingProfile.setFirstName(profileDTO.getFirstName());
        existingProfile.setLastName(profileDTO.getLastName());
        Profile updatedProfile = profileRepository.save(existingProfile);
        return profileMapper.toDTO(updatedProfile);
    }

    /**
     * Deletes a Profile by its ID.
     *
     * @param profileId the ID of the profile to delete
     * @return true if the profile was deleted, false otherwise
     */
    @Transactional
    public boolean deleteProfile(Long profileId) {
        if (profileRepository.existsById(profileId)) {
            profileRepository.deleteById(profileId);
            return true;
        } else {
            throw new RuntimeException("Profile not found");
        }
    }
}
