package dev.bored.profile.mapper;

import dev.bored.profile.dto.ProfileDTO;
import dev.bored.profile.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper that converts between {@link Profile} entities and {@link ProfileDTO} objects.
 *
 * <p>Uses Spring component model so the generated implementation is registered as a Spring bean
 * and can be injected into service classes.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    /**
     * Converts a {@link Profile} entity to a {@link ProfileDTO}.
     *
     * @param profile the profile entity to convert
     * @return the corresponding profile data-transfer object
     */
    ProfileDTO toDTO(Profile profile);

    /**
     * Converts a {@link ProfileDTO} to a {@link Profile} entity.
     *
     * @param profileDTO the profile data-transfer object to convert
     * @return the corresponding profile entity
     */
    Profile toEntity(ProfileDTO profileDTO);
}

