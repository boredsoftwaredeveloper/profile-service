package dev.bored.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * JPA entity representing an achievement or accomplishment linked to a {@link Profile}.
 *
 * <p>Maps to the {@code achievement} table and stores gamified progress data
 * such as title, emoji badge, completion percentage, and display statistics.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Entity
@Table(name = "achievement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Achievement implements Serializable {

    /** Serialization version UID for binary compatibility. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Unique auto-generated identifier for the achievement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Long achievementId;

    /** The {@link Profile} this achievement belongs to. Lazily fetched. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    /** URL-friendly slug identifier for the achievement. Max 50 characters. */
    @Column(name = "slug", nullable = false, length = 50)
    private String slug;

    /** Display title of the achievement. */
    @Column(name = "title", nullable = false)
    private String title;

    /** Optional subtitle providing additional context. */
    @Column(name = "subtitle")
    private String subtitle;

    /** Emoji character used as a visual badge. Max 10 characters. */
    @Column(name = "emoji", length = 10)
    private String emoji;

    /** Completion percentage of the achievement (0–100). */
    @Column(name = "progress_percent", nullable = false)
    private Integer progressPercent;

    /** UI style variant for rendering the achievement card. Max 20 characters. */
    @Column(name = "variant", nullable = false, length = 20)
    private String variant;

    /** Optional label for the achievement statistic (e.g. "Lines of Code"). Max 50 characters. */
    @Column(name = "stat_label", length = 50)
    private String statLabel;

    /** Optional value for the achievement statistic (e.g. "42 k"). Max 50 characters. */
    @Column(name = "stat_value", length = 50)
    private String statValue;

    /** Zero-based sort index controlling display order among achievements. */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
