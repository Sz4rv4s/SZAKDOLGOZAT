package hu.szarvas.football_api.service;

import hu.szarvas.football_api.model.*;
import hu.szarvas.football_api.repository.UserPointsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing leaderboard functionality including:
 * - Processing finished bets and calculating points
 * - Generating weekly and monthly leaderboards
 */
@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final UserPointsRepository userPointsRepository;
    private final SequenceGeneratorService sequenceGenerator;

    /**
     * Processes a finished bet, calculates points, and saves them if earned.
     *
     * @param bet The match score bet to process
     * @param match The completed match with actual results
     */
    public void processFinishedBet(MatchScoreBet bet, Match match) {
        int points = calculatePoints(bet, match);
        if (points > 0) {
            UserPoints userPoints = UserPoints.builder()
                    .id(sequenceGenerator.generateSequence(UserPoints.SEQUENCE_NAME))
                    .userId(bet.getUserId())
                    .points(points)
                    .matchId(bet.getMatchId())
                    .matchDate(match.getUtcDate())
                    .build();
            userPointsRepository.save(userPoints);
        }
    }

    /**
     * Calculates points earned for a bet based on match results.
     * Scoring rules:
     * - 3 points for exact score prediction
     * - 1 point for correct outcome (win/lose/draw)
     * - 0 points for incorrect prediction
     *
     * @param bet The user's bet
     * @param match The actual match results
     * @return Points earned (0-3)
     */
    private int calculatePoints(MatchScoreBet bet, Match match) {
        if (bet.getHomeScoreBet().equals(match.getHomeGoals())
                && bet.getAwayScoreBet().equals(match.getAwayGoals())) {
            return 3;
        }

        boolean isDraw = match.getHomeGoals().equals(match.getAwayGoals());
        boolean betIsDraw = bet.getHomeScoreBet().equals(bet.getAwayScoreBet());

        if (isDraw && betIsDraw) {
            return 1;
        }

        boolean matchHomeWin = match.getHomeGoals() > match.getAwayGoals();
        boolean betHomeWin = bet.getHomeScoreBet() > bet.getAwayScoreBet();

        if (!isDraw && (matchHomeWin == betHomeWin)) {
            return 1;
        }

        return 0;
    }

    /**
     * Generates a weekly leaderboard sorted by total points.
     * The week runs from Monday to Sunday.
     *
     * @return List of leaderboard entries sorted by points (descending)
     */
    public List<LeaderboardEntry> getWeeklyLeaderboard() {
        ZonedDateTime weekStart = ZonedDateTime.now()
                .with(DayOfWeek.MONDAY)
                .truncatedTo(ChronoUnit.DAYS);

        Instant startInstant = weekStart.toInstant();
        Instant endInstant = weekStart.plusWeeks(1).toInstant();

        return getLeaderboardForPeriod(startInstant, endInstant);
    }

    /**
     * Generates a monthly leaderboard sorted by total points.
     * The month runs from 1st to last day of current month.
     *
     * @return List of leaderboard entries sorted by points (descending)
     */
    public List<LeaderboardEntry> getMonthlyLeaderboard() {
        ZonedDateTime monthStart = ZonedDateTime.now()
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);

        Instant startInstant = monthStart.toInstant();
        Instant endInstant = monthStart.plusMonths(1).toInstant();

        return getLeaderboardForPeriod(startInstant, endInstant);
    }

    /**
     * Generates a leaderboard for a specific time period.
     *
     * @param start Start instant of the period
     * @param end End instant of the period
     * @return List of leaderboard entries sorted by points (descending)
     */
    private List<LeaderboardEntry> getLeaderboardForPeriod(Instant start, Instant end) {
        List<UserPoints> periodPoints = userPointsRepository.findByMatchDateBetween(start, end);

        Map<Integer, Integer> userPointsMap = periodPoints.stream()
                .collect(Collectors.groupingBy(
                        UserPoints::getUserId,
                        Collectors.summingInt(UserPoints::getPoints)
                ));

        return userPointsMap.entrySet().stream()
                .map(entry -> LeaderboardEntry.builder()
                        .userId(entry.getKey())
                        .totalPoints(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(LeaderboardEntry::getTotalPoints).reversed())
                .collect(Collectors.toList());
    }
}

