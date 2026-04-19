package dev.bored.profile.service;

/**
 * Cache names used by {@code @Cacheable} / {@code @CacheEvict} annotations
 * across profile-service. Centralised so the Redis key prefix + TTL config
 * in application.yml can reference the same strings.
 */
public final class CacheNames {
    public static final String PROFILE_BY_ID = "profile-by-id";
    public static final String EXPERIENCES_BY_PROFILE = "experiences-by-profile";
    public static final String EXPERIENCE_BY_ID = "experience-by-id";
    public static final String ACHIEVEMENTS_BY_PROFILE = "achievements-by-profile";
    public static final String ACHIEVEMENT_BY_ID = "achievement-by-id";
    public static final String ASPIRATIONS_BY_PROFILE = "aspirations-by-profile";
    public static final String ASPIRATION_BY_ID = "aspiration-by-id";

    private CacheNames() { }
}
