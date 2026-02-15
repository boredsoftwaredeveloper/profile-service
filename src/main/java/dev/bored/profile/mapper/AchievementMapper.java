package dev.bored.profile.mapper;

import dev.bored.profile.dto.AchievementDTO;
import dev.bored.profile.entity.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper that converts between {@link Achievement} entities and {@link AchievementDTO} objects.
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
public interface AchievementMapper {

    /**
     * Converts an {@link Achievement} entity to an {@link AchievementDTO}.
     *
     * @param achievement the achievement entity to convert
     * @return the corresponding achievement data-transfer object
     */
    @Mapping(source = "slug", target = "id")
    @Mapping(source = "profile.profileId", target = "profileId")
    AchievementDTO toDTO(Achievement achievement);

    /**
     * Converts a list of {@link Achievement} entities to a list of {@link AchievementDTO} objects.
     *
     * @param achievements the list of achievement entities to convert
     * @return the corresponding list of achievement data-transfer objects
     */
    List<AchievementDTO> toDTOList(List<Achievement> achievements);

    /**
     * Converts an {@link AchievementDTO} to an {@link Achievement} entity.
     *
     * @param dto the achievement data-transfer object to convert
     * @return the corresponding achievement entity
     */
    @Mapping(source = "id", target = "slug")
    @Mapping(source = "profileId", target = "profile.profileId")
    Achievement toEntity(AchievementDTO dto);
}
