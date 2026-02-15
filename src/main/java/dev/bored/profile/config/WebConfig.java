package dev.bored.profile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration so the Angular front-end can reach the API.
 * <p>
 * Allows cross-origin requests from the local development server and the
 * production domain for all {@code /api/**} endpoints.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registers CORS mappings for the application.
     * <p>
     * Permits {@code GET}, {@code POST}, {@code PUT}, {@code DELETE}, and
     * {@code OPTIONS} requests from the allowed origins with a pre-flight
     * cache duration of one hour.
     * </p>
     *
     * @param registry the {@link CorsRegistry} to add mappings to
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:4200",
                        "https://boredsoftwaredeveloper.xyz",
                        "https://www.boredsoftwaredeveloper.xyz"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
