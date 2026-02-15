package dev.bored.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object matching the front-end {@code Achievement} model.
 *
 * <p>Represents a single achievement/badge associated with a profile.
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
public class AchievementDTO {

    /** Unique database identifier for the achievement record. */
    private Long achievementId;

    /** Identifier of the parent profile this achievement belongs to. */
    private Long profileId;

    /** Client-facing slug identifier; maps to the entity's {@code slug} field. */
    private String id;

    /** Display title of the achievement. */
    private String title;

    /** Secondary descriptive text shown beneath the title. */
    private String subtitle;

    /** Emoji character used as the achievement icon. */
    private String emoji;

    /** Completion percentage (0–100) rendered in progress indicators. */
    private Integer progressPercent;

    /** Visual variant/theme applied to the achievement card on the front end. */
    private String variant;

    /** Label for the statistic displayed on the achievement card. */
    private String statLabel;

    /** Value of the statistic displayed on the achievement card. */
    private String statValue;

    /** Display order among sibling achievement entries (lower = first). */
    private Integer sortOrder;
}
