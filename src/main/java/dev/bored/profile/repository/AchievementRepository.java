package dev.bored.profile.repository;

import dev.bored.profile.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Achievement} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations and adds
 * a derived query method for retrieving achievements by profile, ordered by sort order.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    /**
     * Retrieves all achievements belonging to the specified profile, ordered by
     * {@code sortOrder} ascending.
     *
     * @param profileId the ID of the profile whose achievements are requested
     * @return a list of achievements sorted by {@code sortOrder} in ascending order
     */
    List<Achievement> findByProfile_ProfileIdOrderBySortOrderAsc(Long profileId);
}
