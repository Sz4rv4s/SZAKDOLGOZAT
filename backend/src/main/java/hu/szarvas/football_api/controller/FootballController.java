package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.dto.response.DefaultResponseDTO;
import hu.szarvas.football_api.model.BetStatus;
import hu.szarvas.football_api.model.MatchScoreBet;
import hu.szarvas.football_api.service.FootballService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/football")
@RequiredArgsConstructor
public class FootballController {
    private final FootballService footballService;

    @PostMapping("/bets/match-score")
    public ResponseEntity<DefaultResponseDTO> makeMatchScoreBet(
            @RequestBody MatchScoreBet bet) {
        bet.setDate(Instant.now());
        bet.setStatus(BetStatus.LIVE);
        return footballService.makeMatchScoreBet(bet);
    }
}
