package dev.bored.profile.mapper;

import dev.bored.profile.dto.AspirationDTO;
import dev.bored.profile.entity.Aspiration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper that converts between {@link Aspiration} entities and {@link AspirationDTO} objects.
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
public interface AspirationMapper {

    /**
     * Converts an {@link Aspiration} entity to an {@link AspirationDTO}.
     *
     * @param aspiration the aspiration entity to convert
     * @return the corresponding aspiration data-transfer object
     */
    @Mapping(source = "slug", target = "id")
    @Mapping(source = "profile.profileId", target = "profileId")
    AspirationDTO toDTO(Aspiration aspiration);

    /**
     * Converts a list of {@link Aspiration} entities to a list of {@link AspirationDTO} objects.
     *
     * @param aspirations the list of aspiration entities to convert
     * @return the corresponding list of aspiration data-transfer objects
     */
    List<AspirationDTO> toDTOList(List<Aspiration> aspirations);

    /**
     * Converts an {@link AspirationDTO} to an {@link Aspiration} entity.
     *
     * @param dto the aspiration data-transfer object to convert
     * @return the corresponding aspiration entity
     */
    @Mapping(source = "id", target = "slug")
    @Mapping(source = "profileId", target = "profile.profileId")
    Aspiration toEntity(AspirationDTO dto);
}
