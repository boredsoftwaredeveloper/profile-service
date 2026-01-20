package dev.bored.profile.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Data Transfer Object for Profile entity.
 */
@Data
@Builder
public class ProfileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long profileId;
    private String firstName;
    private String lastName;
}
