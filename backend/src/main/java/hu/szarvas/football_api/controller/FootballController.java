package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.dto.response.CompetitionResponseDTO;
import hu.szarvas.football_api.dto.response.DefaultResponseDTO;
import hu.szarvas.football_api.dto.response.MatchResponseDTO;
import hu.szarvas.football_api.dto.response.MatchScoreBetResponseDTO;
import hu.szarvas.football_api.model.BetStatus;
import hu.szarvas.football_api.model.MatchScoreBet;
import hu.szarvas.football_api.dto.request.UpdateBetDTO;
import hu.szarvas.football_api.service.FootballService;
import hu.szarvas.football_api.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Controller for handling football-related operations such as betting, fetching leagues and matches.
 */
@RestController
@RequestMapping("/api/football")
@RequiredArgsConstructor
public class FootballController {
    private final FootballService footballService;
    private final SequenceGeneratorService sequenceGenerator;

    /**
     * Creates a match score bet.
     * @param bet match score bet details
     * @return response indicating success or failure
     */
    @PostMapping("/bets/match-score")
    public ResponseEntity<DefaultResponseDTO> makeMatchScoreBet(@RequestBody MatchScoreBet bet) {
        bet.setId(sequenceGenerator.generateSequence(MatchScoreBet.SEQUENCE_NAME));
        bet.setDate(Instant.now());
        bet.setStatus(BetStatus.LIVE);
        return footballService.makeMatchScoreBet(bet);
    }

    /**
     * Retrieves a match score bet for a specific user and match.
     * @param userId user's ID
     * @param matchId match ID
     * @return the bet if exists
     */
    @GetMapping("/bets/match-score/{userId}/{matchId}")
    public ResponseEntity<?> getMatchScoreBet(@PathVariable Integer userId, @PathVariable Integer matchId) {
        return footballService.getMatchScoreBet(userId, matchId);
    }

    /**
     * Updates a match score bet.
     * @param userId user who made the bet
     * @param updatedBet new bet details
     * @return response indicating update success
     */
    @PatchMapping("/bets/match-score/{userId}")
    public ResponseEntity<DefaultResponseDTO> updateMatchScoreBet(
            @PathVariable Integer userId,
            @RequestBody UpdateBetDTO updatedBet
    ) {
        return footballService.updateMatchScoreBet(userId, updatedBet);
    }

    /**
     * Cancels a match score bet.
     * @param userId user who made the bet
     * @param matchId match ID
     * @return response indicating cancellation result
     */
    @DeleteMapping("/bets/match-score/{userId}/{matchId}")
    public ResponseEntity<DefaultResponseDTO> cancelMatchScoreBet(@PathVariable Integer userId, @PathVariable Integer matchId) {
        return footballService.cancelMatchScoreBet(userId, matchId);
    }

    /**
     * Fetches available football leagues.
     * @return list of leagues
     */
    @GetMapping("/get/leagues")
    public ResponseEntity<CompetitionResponseDTO> getLeagues() {
        return footballService.getLeagues();
    }

    /**
     * Gets upcoming matches for a given league.
     * @param leagueId league ID
     * @return list of upcoming matches
     */
    @GetMapping("/get/{leagueId}/upcoming-matches")
    public ResponseEntity<MatchResponseDTO> getUpcomingMatchesForLeague(@PathVariable Integer leagueId) {
        return footballService.getUpcomingMatchesForLeague(leagueId);
    }

    /**
     * Retrieves all match score bets made by a specific user.
     * @param userId user ID
     * @return list of match score bets
     */
    @GetMapping("/get/{userId}/bets/match-score")
    public ResponseEntity<MatchScoreBetResponseDTO> getMatchScoreBet(@PathVariable Integer userId) {
        return footballService.getMatchScoreBetsForUser(userId);
    }
}