package dev.bored.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object matching the front-end {@code Aspiration} model.
 *
 * <p>Represents a single aspiration/goal associated with a profile.
 * Fields are aligned with the Angular front-end model so the JSON
 * structure can be consumed directly by the client.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AspirationDTO {

    /** Unique database identifier for the aspiration record. */
    private Long aspirationId;

    /** Identifier of the parent profile this aspiration belongs to. */
    private Long profileId;

    /** Client-facing slug identifier; maps to the entity's {@code slug} field. */
    private String id;

    /** Display title of the aspiration. */
    private String title;

    /** Secondary descriptive text shown beneath the title. */
    private String subtitle;

    /** Short status label (e.g. "In Progress", "Planned"). */
    private String statusText;

    /** Completion percentage (0–100) rendered in progress indicators. */
    private Integer progressPercent;

    /** Visual variant/theme applied to the aspiration card on the front end. */
    private String variant;

    /** Text displayed in the card footer. */
    private String footerText;

    /** Whether the card's progress indicator should animate on render. */
    private Boolean animated;

    /** Display order among sibling aspiration entries (lower = first). */
    private Integer sortOrder;
}
