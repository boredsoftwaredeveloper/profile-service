package dev.bored.profile.repository;

import dev.bored.profile.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Experience} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations and adds
 * a derived query method for retrieving experiences by profile, ordered by sort order.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    /**
     * Retrieves all experiences belonging to the specified profile, ordered by
     * {@code sortOrder} ascending.
     *
     * @param profileId the ID of the profile whose experiences are requested
     * @return a list of experiences sorted by {@code sortOrder} in ascending order
     */
    List<Experience> findByProfile_ProfileIdOrderBySortOrderAsc(Long profileId);
}
