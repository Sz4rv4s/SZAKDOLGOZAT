package hu.szarvas.football_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Football API application.
 * <p>
 * This class bootstraps and launches the Spring Boot application with all its
 * configured components, services, and configurations. The {@code @SpringBootApplication}
 * annotation enables:
 * <ul>
 *   <li>Auto-configuration of Spring components</li>
 *   <li>Component scanning within the package and sub-packages</li>
 *   <li>Ability to define additional beans in the application context</li>
 * </ul>
 *
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 */
@SpringBootApplication
public class FootballAPIApplication {

	/**
	 * Main method that serves as the entry point for the application.
	 *
	 * @param args Command line arguments passed during application startup.
	 *             These can be used to configure the application behavior.
	 */
	public static void main(String[] args) {
		SpringApplication.run(FootballAPIApplication.class, args);
	}
}