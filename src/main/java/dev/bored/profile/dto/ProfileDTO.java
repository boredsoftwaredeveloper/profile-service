package dev.bored.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Data Transfer Object for the Profile entity.
 *
 * <p>Used for API request/response serialization of profile data.
 * Carries core identity and display information for a user profile
 * between the service layer and REST controllers.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the profile. */
    private Long profileId;

    /** User's first (given) name. */
    private String firstName;

    /** User's last (family) name. */
    private String lastName;

    /** URL pointing to the user's profile photo. */
    private String photoUrl;

    /** Current status or tagline displayed on the profile. */
    private String status;
}
