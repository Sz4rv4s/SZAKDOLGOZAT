package hu.szarvas.football_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Enables scheduling support for periodic tasks in the application.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
}
