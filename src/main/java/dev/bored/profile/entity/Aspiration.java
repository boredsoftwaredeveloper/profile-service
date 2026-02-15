package dev.bored.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * JPA entity representing a future goal or aspiration linked to a {@link Profile}.
 *
 * <p>Maps to the {@code aspiration} table and tracks long-term objectives
 * including progress percentage, display variants, and animation preferences.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Entity
@Table(name = "aspiration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aspiration implements Serializable {

    /** Serialization version UID for binary compatibility. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Unique auto-generated identifier for the aspiration. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aspiration_id")
    private Long aspirationId;

    /** The {@link Profile} this aspiration belongs to. Lazily fetched. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    /** URL-friendly slug identifier for the aspiration. Max 50 characters. */
    @Column(name = "slug", nullable = false, length = 50)
    private String slug;

    /** Display title of the aspiration. */
    @Column(name = "title", nullable = false)
    private String title;

    /** Optional subtitle providing additional context. */
    @Column(name = "subtitle")
    private String subtitle;

    /** Short status label shown on the aspiration card (e.g. "In Progress"). Max 100 characters. */
    @Column(name = "status_text", length = 100)
    private String statusText;

    /** Completion percentage of the aspiration (0–100). */
    @Column(name = "progress_percent", nullable = false)
    private Integer progressPercent;

    /** UI style variant for rendering the aspiration card. Max 20 characters. */
    @Column(name = "variant", nullable = false, length = 20)
    private String variant;

    /** Optional text displayed in the card footer area. */
    @Column(name = "footer_text")
    private String footerText;

    /** Whether the aspiration card should use animated transitions. */
    @Column(name = "animated", nullable = false)
    private Boolean animated;

    /** Zero-based sort index controlling display order among aspirations. */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
