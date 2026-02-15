package dev.bored.profile.mapper;

import dev.bored.profile.dto.ExperienceDTO;
import dev.bored.profile.entity.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper that converts between {@link Experience} entities and {@link ExperienceDTO} objects.
 *
 * <p>Field mappings:</p>
 * <ul>
 *   <li>{@code slug} (entity) &harr; {@code id} (DTO)</li>
 *   <li>{@code profile.profileId} (entity) &harr; {@code profileId} (DTO)</li>
 * </ul>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExperienceMapper {

    /**
     * Converts an {@link Experience} entity to an {@link ExperienceDTO}.
     *
     * @param experience the experience entity to convert
     * @return the corresponding experience data-transfer object
     */
    @Mapping(source = "slug", target = "id")
    @Mapping(source = "profile.profileId", target = "profileId")
    ExperienceDTO toDTO(Experience experience);

    /**
     * Converts a list of {@link Experience} entities to a list of {@link ExperienceDTO} objects.
     *
     * @param experiences the list of experience entities to convert
     * @return the corresponding list of experience data-transfer objects
     */
    List<ExperienceDTO> toDTOList(List<Experience> experiences);

    /**
     * Converts an {@link ExperienceDTO} to an {@link Experience} entity.
     *
     * @param dto the experience data-transfer object to convert
     * @return the corresponding experience entity
     */
    @Mapping(source = "id", target = "slug")
    @Mapping(source = "profileId", target = "profile.profileId")
    Experience toEntity(ExperienceDTO dto);
}
