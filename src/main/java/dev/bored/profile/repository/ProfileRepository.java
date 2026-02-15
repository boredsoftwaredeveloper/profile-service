package dev.bored.profile.repository;

import dev.bored.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Profile} entities.
 *
 * <p>Provides standard CRUD operations inherited from {@link JpaRepository}.
 * No custom query methods are defined; all persistence operations use the
 * built-in JPA repository methods.</p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}