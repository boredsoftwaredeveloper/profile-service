package dev.bored.profile.controller;

import dev.bored.profile.dto.AchievementDTO;
import dev.bored.profile.service.AchievementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing achievement records associated with user profiles.
 * <p>
 * Exposes endpoints under {@code /api/v1/achievements} to create, read,
 * update, and delete achievement entries.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    /**
     * Retrieves all achievements belonging to a given profile.
     *
     * @param profileId the ID of the profile whose achievements are requested
     *                  (defaults to {@code 1})
     * @return a list of {@link AchievementDTO} objects for the specified profile
     */
    @GetMapping
    public List<AchievementDTO> getAchievementsByProfileId(
            @RequestParam(defaultValue = "1") Long profileId) {
        return achievementService.getAchievementsByProfileId(profileId);
    }

    /**
     * Retrieves a single achievement by its ID.
     *
     * @param achievementId the ID of the achievement to retrieve
     * @return the {@link AchievementDTO} corresponding to the given ID
     */
    @GetMapping("/{achievementId}")
    public AchievementDTO getAchievementById(@PathVariable Long achievementId) {
        return achievementService.getAchievementById(achievementId);
    }

    /**
     * Creates a new achievement record.
     *
     * @param dto the {@link AchievementDTO} containing the achievement data to save
     * @return the saved {@link AchievementDTO}
     */
    @PostMapping
    public AchievementDTO addAchievement(@RequestBody AchievementDTO dto) {
        return achievementService.addAchievement(dto);
    }

    /**
     * Updates an existing achievement by its ID.
     *
     * @param achievementId the ID of the achievement to update
     * @param dto           the {@link AchievementDTO} with updated data
     * @return the updated {@link AchievementDTO}
     */
    @PutMapping("/{achievementId}")
    public AchievementDTO updateAchievement(@PathVariable Long achievementId, @RequestBody AchievementDTO dto) {
        return achievementService.updateAchievement(achievementId, dto);
    }

    /**
     * Deletes an achievement by its ID.
     *
     * @param achievementId the ID of the achievement to delete
     * @return {@code true} if the achievement was deleted, {@code false} otherwise
     */
    @DeleteMapping("/{achievementId}")
    public boolean deleteAchievement(@PathVariable Long achievementId) {
        return achievementService.deleteAchievement(achievementId);
    }
}
