package dev.bored.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object matching the front-end {@code ExperienceEntry} model.
 *
 * <p>Represents a single professional-experience entry associated with a
 * profile. Fields are aligned with the Angular front-end model so the
 * JSON structure can be consumed directly by the client.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {

    /** Unique database identifier for the experience record. */
    private Long experienceId;

    /** Identifier of the parent profile this experience belongs to. */
    private Long profileId;

    /** Client-facing slug identifier; maps to the entity's {@code slug} field. */
    private String id;

    /** Name of the company or organisation. */
    private String company;

    /** Job title or role held at the company. */
    private String role;

    /** CSS style variant applied to the role badge on the front end. */
    private String roleStyle;

    /** Free-text description of responsibilities and accomplishments. */
    private String description;

    /** Date the role started. */
    private LocalDate startDate;

    /** Date the role ended; {@code null} indicates a current position. */
    private LocalDate endDate;

    /** Display order among sibling experience entries (lower = first). */
    private Integer sortOrder;
}
