package dev.bored.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * JPA entity representing a professional work experience entry linked to a {@link Profile}.
 *
 * <p>Maps to the {@code experience} table and captures details about a single
 * position held by the user, including company, role, date range, and display
 * ordering metadata.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Entity
@Table(name = "experience")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience implements Serializable {

    /** Serialization version UID for binary compatibility. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Unique auto-generated identifier for the experience entry. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long experienceId;

    /** The {@link Profile} this experience belongs to. Lazily fetched. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    /** URL-friendly slug identifier for the experience. Max 50 characters. */
    @Column(name = "slug", nullable = false, length = 50)
    private String slug;

    /** Name of the company or organisation. Max 100 characters. */
    @Column(name = "company", nullable = false, length = 100)
    private String company;

    /** Job title or role held at the company. */
    @Column(name = "role", nullable = false)
    private String role;

    /** CSS/UI style variant applied to the role badge. Max 20 characters. */
    @Column(name = "role_style", nullable = false, length = 20)
    private String roleStyle;

    /** Optional free-text description of responsibilities and accomplishments. */
    @Column(name = "description")
    private String description;

    /** Date the position started. May be {@code null} if unknown. */
    @Column(name = "start_date")
    private LocalDate startDate;

    /** Date the position ended. {@code null} indicates a current position. */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** Zero-based sort index controlling display order among experiences. */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
