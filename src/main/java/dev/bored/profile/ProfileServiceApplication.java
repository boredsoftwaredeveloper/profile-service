package dev.bored.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Profile Service Spring Boot application.
 * <p>
 * Bootstraps the Spring context, auto-configures beans, and starts
 * the embedded web server.
 * </p>
 *
 * @author Bored Software Developer
 * @since 2026-02-15
 */
@SpringBootApplication
public class ProfileServiceApplication {

	/**
	 * Application entry point that launches the Spring Boot application.
	 *
	 * @param args command-line arguments passed at startup
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProfileServiceApplication.class, args);
	}

}
