package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.service.DataFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for fetching and storing football data from external APIs.
 */
@RestController
@RequestMapping("/api/data")
@Slf4j
@RequiredArgsConstructor
public class DataController {
    private final DataFetchService dataFetchService;

    /**
     * Fetches and stores area data from external source.
     * @return 200 OK if successful
     */
    @PostMapping("/fetch-areas")
    public ResponseEntity<Void> fetchAreas() {
        try {
            dataFetchService.fetchAndSaveAreas();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Fetches and stores competitions and their seasons.
     * @return 200 OK if successful
     */
    @PostMapping("/fetch-competitions-with-seasons")
    public ResponseEntity<Void> fetchCompetitionsWithSeasons() {
        try {
            dataFetchService.fetchAndSaveCompetitionsWithSeasons();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Fetches and stores teams and their players.
     * @return 200 OK if successful
     */
    @PostMapping("/fetch-teams-with-players")
    public ResponseEntity<Void> fetchTeamsWithPlayers() {
        try {
            dataFetchService.fetchAndSaveTeamsWithPlayers();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Fetches and stores football match data.
     * @return 200 OK if successful
     */
    @PostMapping("/fetch-matches")
    public ResponseEntity<Void> fetchMatches() {
        try {
            dataFetchService.fetchAndSaveMatches();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}