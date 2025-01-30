package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.dto.response.CompetitionResponseDTO;
import hu.szarvas.football_api.dto.response.DefaultResponseDTO;
import hu.szarvas.football_api.dto.response.MatchResponseDTO;
import hu.szarvas.football_api.model.BetStatus;
import hu.szarvas.football_api.model.MatchScoreBet;
import hu.szarvas.football_api.service.FootballService;
import hu.szarvas.football_api.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/football")
@RequiredArgsConstructor
public class FootballController {
    private final FootballService footballService;
    private final SequenceGeneratorService sequenceGenerator;

    @PostMapping("/bets/match-score")
    public ResponseEntity<DefaultResponseDTO> makeMatchScoreBet(
            @RequestBody MatchScoreBet bet) {
        bet.setId(sequenceGenerator.generateSequence(MatchScoreBet.SEQUENCE_NAME));
        bet.setDate(Instant.now());
        bet.setStatus(BetStatus.LIVE);
        return footballService.makeMatchScoreBet(bet);
    }

    @GetMapping("/get/leagues")
    public ResponseEntity<CompetitionResponseDTO> getLeagues() {
        return footballService.getLeagues();
    }

    @GetMapping("/get/{leagueId}/upcomingmatches")
    public ResponseEntity<MatchResponseDTO> getUpcomingMatchesForLeague(@PathVariable Integer leagueId) {
        return footballService.getUpcomingMatchesForLeague(leagueId);
    }
}
