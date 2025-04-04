package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.model.LeaderboardEntry;
import hu.szarvas.football_api.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for accessing leaderboard data based on user betting performance.
 */
@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    /**
     * Gets the weekly leaderboard standings.
     * @return list of top users for the week
     */
    @GetMapping("/weekly")
    public ResponseEntity<List<LeaderboardEntry>> getWeeklyLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getWeeklyLeaderboard());
    }

    /**
     * Gets the monthly leaderboard standings.
     * @return list of top users for the month
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<LeaderboardEntry>> getMonthlyLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getMonthlyLeaderboard());
    }
}
