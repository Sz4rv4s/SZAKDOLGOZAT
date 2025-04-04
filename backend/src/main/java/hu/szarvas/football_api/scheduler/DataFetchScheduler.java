package hu.szarvas.football_api.scheduler;

import hu.szarvas.football_api.service.DataFetchService;
import hu.szarvas.football_api.service.FootballService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler class that handles periodic fetching and updating of football-related data.
 * It uses Spring's @Scheduled annotation to run tasks at fixed intervals.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DataFetchScheduler {

    private final DataFetchService dataFetchService;
    private final FootballService footballService;

    /**
     * Scheduled task that runs **monthly on the 1st day at 2:00 AM**.
     * <p>
     * It fetches and saves:
     * - Areas
     * - Competitions with seasons
     * - Teams with players
     * <p>
     * Thread.sleep() is used to avoid hitting rate limits between API calls.
     */
    @Scheduled(cron = "0 0 2 1 * *")
    public void scheduledMonthlyDataFetch() {
        try {
            log.info("Starting monthly scheduled data fetch");

            log.info("Scheduled fetching of areas");
            dataFetchService.fetchAndSaveAreas();
            Thread.sleep(60000); // 60s delay to avoid hitting rate limits

            log.info("Scheduled fetching of competitions and seasons");
            dataFetchService.fetchAndSaveCompetitionsWithSeasons();
            Thread.sleep(60000); // 60s delay to avoid hitting rate limits

            log.info("Scheduled fetching of teams and players");
            dataFetchService.fetchAndSaveTeamsWithPlayers();

            log.info("Finished monthly scheduled data fetch");
        } catch (Exception e) {
            log.error("Error during monthly scheduled data fetch", e);
        }
    }

    /**
     * Scheduled task that runs **every 15 minutes**.
     * <p>
     * It fetches and saves the latest match data and updates bet statuses accordingly.
     */
    @Scheduled(cron = "0 0/15 * * * *")
    public void scheduledFrequentlyDataFetch() {
        try {
            log.info("Starting frequently scheduled data fetch");

            log.info("Scheduled fetching of matches");
            dataFetchService.fetchAndSaveMatches();

            log.info("Scheduled fetching of bet statuses");
            footballService.updateBetStatuses();

            log.info("Finished frequently scheduled data fetch");
        } catch (Exception e) {
            log.error("Error during frequently scheduled data fetch", e);
        }
    }
}
