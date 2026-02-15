package dev.bored.profile.controller;

import dev.bored.profile.dto.ProfileDTO;
import dev.bored.profile.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing profile data via CRUD operations.
 * <p>
 * Exposes endpoints under {@code /api/v1/profiles} to create, read,
 * update, and delete user profiles.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Retrieves a Profile by its ID.
     *
     * @param profileId the ID of the profile to retrieve
     * @return the ProfileDTO corresponding to the given ID
     */
    @GetMapping("/{profileId}")
    public ProfileDTO getProfileById(@PathVariable Long profileId) {
        return profileService.getProfileById(profileId);
    }

    /**
     * Saves a Profile.
     *
     * @param profileDTO the ProfileDTO to save
     * @return the saved ProfileDTO
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ProfileDTO addProfile(@RequestBody ProfileDTO profileDTO) {
        return profileService.addProfile(profileDTO);
    }

    /**
     * Updates a Profile by its ID.
     *
     * @param profileId  the ID of the profile to update
     * @param profileDTO the ProfileDTO with updated data
     * @return the updated ProfileDTO
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{profileId}")
    public ProfileDTO updateProfile(@PathVariable Long profileId, @RequestBody ProfileDTO profileDTO) {
        return profileService.updateProfile(profileId, profileDTO);
    }

    /**
     * Deletes a Profile by its ID.
     *
     * @param profileId the ID of the profile to delete
     * @return true if the profile was deleted, false otherwise
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public boolean deleteProfile(Long profileId) {
        return profileService.deleteProfile(profileId);
    }
}
