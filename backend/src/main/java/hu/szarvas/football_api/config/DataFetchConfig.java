package hu.szarvas.football_api.config;

import hu.szarvas.football_api.service.DataFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for triggering the initial football data fetch on application startup.
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataFetchConfig {

    private final DataFetchService dataFetchService;

    /**
     * CommandLineRunner that initiates fetching of areas, competitions, teams, and players.
     *
     * @return CommandLineRunner instance
     */
    @Bean
    public CommandLineRunner initDataFetch() {
        return args -> {
            try {
                log.info("Starting initial data fetch on application startup");

                log.info("Fetching areas");
                dataFetchService.fetchAndSaveAreas();
                Thread.sleep(5000);

                log.info("Fetching competitions and seasons");
                dataFetchService.fetchAndSaveCompetitionsWithSeasons();
                Thread.sleep(5000);

                log.info("Fetching teams and players");
                dataFetchService.fetchAndSaveTeamsWithPlayers();

                log.info("Finished initial data fetch on application startup");
            } catch (Exception e) {
                log.error("Error during initial data fetch", e);
            }
        };
    }
}