package dev.bored.profile.repository;

import dev.bored.profile.entity.Aspiration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Aspiration} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations and adds
 * a derived query method for retrieving aspirations by profile, ordered by sort order.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Repository
public interface AspirationRepository extends JpaRepository<Aspiration, Long> {

    /**
     * Retrieves all aspirations belonging to the specified profile, ordered by
     * {@code sortOrder} ascending.
     *
     * @param profileId the ID of the profile whose aspirations are requested
     * @return a list of aspirations sorted by {@code sortOrder} in ascending order
     */
    List<Aspiration> findByProfile_ProfileIdOrderBySortOrderAsc(Long profileId);
}
