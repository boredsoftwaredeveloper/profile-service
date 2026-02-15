package dev.bored.profile.controller;

import dev.bored.profile.dto.ExperienceDTO;
import dev.bored.profile.service.ExperienceService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing experience records associated with user profiles.
 * <p>
 * Exposes endpoints under {@code /api/v1/experiences} to create, read,
 * update, and delete experience entries.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    /**
     * Retrieves all experiences belonging to a given profile.
     *
     * @param profileId the ID of the profile whose experiences are requested
     *                  (defaults to {@code 1})
     * @return a list of {@link ExperienceDTO} objects for the specified profile
     */
    @GetMapping
    public List<ExperienceDTO> getExperiencesByProfileId(
            @RequestParam(defaultValue = "1") Long profileId) {
        return experienceService.getExperiencesByProfileId(profileId);
    }

    /**
     * Retrieves a single experience by its ID.
     *
     * @param experienceId the ID of the experience to retrieve
     * @return the {@link ExperienceDTO} corresponding to the given ID
     */
    @GetMapping("/{experienceId}")
    public ExperienceDTO getExperienceById(@PathVariable Long experienceId) {
        return experienceService.getExperienceById(experienceId);
    }

    /**
     * Creates a new experience record.
     *
     * @param dto the {@link ExperienceDTO} containing the experience data to save
     * @return the saved {@link ExperienceDTO}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ExperienceDTO addExperience(@RequestBody ExperienceDTO dto) {
        return experienceService.addExperience(dto);
    }

    /**
     * Updates an existing experience by its ID.
     *
     * @param experienceId the ID of the experience to update
     * @param dto          the {@link ExperienceDTO} with updated data
     * @return the updated {@link ExperienceDTO}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{experienceId}")
    public ExperienceDTO updateExperience(@PathVariable Long experienceId, @RequestBody ExperienceDTO dto) {
        return experienceService.updateExperience(experienceId, dto);
    }

    /**
     * Deletes an experience by its ID.
     *
     * @param experienceId the ID of the experience to delete
     * @return {@code true} if the experience was deleted, {@code false} otherwise
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{experienceId}")
    public boolean deleteExperience(@PathVariable Long experienceId) {
        return experienceService.deleteExperience(experienceId);
    }
}
