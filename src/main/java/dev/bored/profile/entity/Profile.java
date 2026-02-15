package dev.bored.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * JPA entity representing a user profile.
 *
 * <p>Maps to the {@code profile} table and stores core identity information
 * such as the user's name, profile photo, and current status message.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Entity
@Table(name = "profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements Serializable {

    /** Serialization version UID for binary compatibility. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Unique auto-generated identifier for the profile. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    /** User's first (given) name. Must not be {@code null}. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** User's last (family) name. Must not be {@code null}. */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** URL pointing to the user's profile photo. May be up to 500 characters. */
    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    /** Free-text status message displayed on the user's profile. */
    @Column(name = "status")
    private String status;
}
