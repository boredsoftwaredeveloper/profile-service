package dev.bored.profile.repository;

import dev.bored.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Profile entity.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}