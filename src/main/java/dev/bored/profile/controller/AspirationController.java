package dev.bored.profile.controller;

import dev.bored.profile.dto.AspirationDTO;
import dev.bored.profile.service.AspirationService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing aspiration records associated with user profiles.
 * <p>
 * Exposes endpoints under {@code /api/v1/aspirations} to create, read,
 * update, and delete aspiration entries.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/aspirations")
public class AspirationController {

    private final AspirationService aspirationService;

    /**
     * Retrieves all aspirations belonging to a given profile.
     *
     * @param profileId the ID of the profile whose aspirations are requested
     *                  (defaults to {@code 1})
     * @return a list of {@link AspirationDTO} objects for the specified profile
     */
    @GetMapping
    public List<AspirationDTO> getAspirationsByProfileId(
            @RequestParam(defaultValue = "1") Long profileId) {
        return aspirationService.getAspirationsByProfileId(profileId);
    }

    /**
     * Retrieves a single aspiration by its ID.
     *
     * @param aspirationId the ID of the aspiration to retrieve
     * @return the {@link AspirationDTO} corresponding to the given ID
     */
    @GetMapping("/{aspirationId}")
    public AspirationDTO getAspirationById(@PathVariable Long aspirationId) {
        return aspirationService.getAspirationById(aspirationId);
    }

    /**
     * Creates a new aspiration record.
     *
     * @param dto the {@link AspirationDTO} containing the aspiration data to save
     * @return the saved {@link AspirationDTO}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public AspirationDTO addAspiration(@RequestBody AspirationDTO dto) {
        return aspirationService.addAspiration(dto);
    }

    /**
     * Updates an existing aspiration by its ID.
     *
     * @param aspirationId the ID of the aspiration to update
     * @param dto          the {@link AspirationDTO} with updated data
     * @return the updated {@link AspirationDTO}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{aspirationId}")
    public AspirationDTO updateAspiration(@PathVariable Long aspirationId, @RequestBody AspirationDTO dto) {
        return aspirationService.updateAspiration(aspirationId, dto);
    }

    /**
     * Deletes an aspiration by its ID.
     *
     * @param aspirationId the ID of the aspiration to delete
     * @return {@code true} if the aspiration was deleted, {@code false} otherwise
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{aspirationId}")
    public boolean deleteAspiration(@PathVariable Long aspirationId) {
        return aspirationService.deleteAspiration(aspirationId);
    }
}
