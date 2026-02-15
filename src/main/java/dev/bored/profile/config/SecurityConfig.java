package dev.bored.profile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Spring Security configuration for the Profile REST API.
 * <p>
 * Validates Supabase-issued JWTs (HS256) on every request. Read endpoints
 * are public so anyone can view the portfolio. Write endpoints require a
 * valid JWT — only the authenticated portfolio owner can mutate data.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    /**
     * Defines the HTTP security filter chain.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the built {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public: all GET requests (portfolio is read-only for visitors)
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Public: actuator health + info
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Public: Swagger / OpenAPI docs
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        // Everything else (POST, PUT, DELETE) requires a valid JWT
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * Creates a JWT decoder that validates Supabase tokens using HS256.
     * <p>
     * Supabase signs JWTs with a symmetric secret (HMAC-SHA256), not RSA.
     * The secret is injected from the {@code SUPABASE_JWT_SECRET} env var.
     * </p>
     *
     * @return the configured {@link JwtDecoder}
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
