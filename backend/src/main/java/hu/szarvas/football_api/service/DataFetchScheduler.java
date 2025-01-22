package hu.szarvas.football_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataFetchScheduler {
    private final DataFetchService dataFetchService;

    @Scheduled(cron = "0 0 2 1 * *")
    public void scheduledMonthlyDataFetch() {
        try {
            log.info("Starting monthly scheduled data fetch");

            log.info("Scheduled fetching of areas");
            dataFetchService.fetchAndSaveAreas();
            Thread.sleep(60000);

            log.info("Scheduled fetching of competitions and seasons");
            dataFetchService.fetchAndSaveCompetitionsWithSeasons();
            Thread.sleep(60000);

            log.info("Scheduled fetching of teams and players");
            dataFetchService.fetchAndSaveTeamsWithPlayers();

            log.info("Finished monthly scheduled data fetch");
        } catch (Exception e) {
            log.error("Error during monthly scheduled data fetch", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledDailyDataFetch() {
        try {
            log.info("Starting daily scheduled data fetch");

            log.info("Scheduled fetching of matches");
            dataFetchService.fetchAndSaveAreas();

            log.info("Finished daily scheduled data fetch");
        } catch (Exception e) {
            log.error("Error during daily scheduled data fetch", e);
        }
    }
}
