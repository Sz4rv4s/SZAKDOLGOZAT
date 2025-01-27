package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.model.LeaderboardEntry;
import hu.szarvas.football_api.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/football")
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping("/leaderboard/weekly")
    public ResponseEntity<List<LeaderboardEntry>> getWeeklyLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getWeeklyLeaderboard());
    }

    @GetMapping("/leaderboard/monthly")
    public ResponseEntity<List<LeaderboardEntry>> getMonthlyLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getMonthlyLeaderboard());
    }
}
