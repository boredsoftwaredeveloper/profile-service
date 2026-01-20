package dev.bored.profile.mapper;

import dev.bored.profile.dto.ProfileDTO;
import dev.bored.profile.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for Profile entity and ProfileDTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    /**
     * Converts Profile entity to ProfileDTO.
     *
     * @param profile the profile entity
     * @return the profile DTO
     */
    ProfileDTO toDTO(Profile profile);

    /**
     * Converts ProfileDTO to Profile entity.
     *
     * @param profileDTO the profile DTO
     * @return the profile entity
     */
    Profile toEntity(ProfileDTO profileDTO);
}

