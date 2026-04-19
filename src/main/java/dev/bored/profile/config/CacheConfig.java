package dev.bored.profile.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Enables Spring's cache abstraction + installs a fail-open error handler.
 *
 * <p>With a Redis backend, any network hiccup between Cloud Run and Upstash
 * would normally propagate as a {@code RuntimeException} through the service
 * layer. We'd rather log it and serve a cache miss — the DB is our source of
 * truth. The custom {@link CacheErrorHandler} swallows Redis read/write/evict
 * errors and lets the wrapped method run as if the cache were empty.</p>
 *
 * <p>When Redis is not configured (local dev, auto-configs excluded),
 * Spring falls back to {@code ConcurrentMapCacheManager} and this handler
 * is effectively a no-op.</p>
 */
@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public CacheErrorHandler cacheErrorHandler() {
        return new FailOpenCacheErrorHandler();
    }

    /**
     * Swap the default JDK-serialization value serializer for JSON so cached
     * values are readable in redis-cli and DTOs don't need {@code Serializable}.
     * Polymorphic type info is written so Jackson can rebuild the concrete
     * class on read (needed for {@code List<DTO>} return types).
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer jsonRedisCacheManagerCustomizer() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType("dev.bored.")
                                .allowIfSubType("java.util.")
                                .allowIfSubType("java.time.")
                                .build(),
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);
        RedisCacheConfiguration jsonConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
        return builder -> builder.cacheDefaults(jsonConfig);
    }

    /** Logs cache backend errors at WARN + continues as if the cache were empty. */
    static final class FailOpenCacheErrorHandler extends SimpleCacheErrorHandler {
        @Override
        public void handleCacheGetError(RuntimeException ex, Cache cache, Object key) {
            log.warn("Cache get failed for {}[{}] — falling through to loader", cache.getName(), key, ex);
        }

        @Override
        public void handleCachePutError(RuntimeException ex, Cache cache, Object key, Object value) {
            log.warn("Cache put failed for {}[{}]", cache.getName(), key, ex);
        }

        @Override
        public void handleCacheEvictError(RuntimeException ex, Cache cache, Object key) {
            log.warn("Cache evict failed for {}[{}]", cache.getName(), key, ex);
        }

        @Override
        public void handleCacheClearError(RuntimeException ex, Cache cache) {
            log.warn("Cache clear failed for {}", cache.getName(), ex);
        }
    }
}
