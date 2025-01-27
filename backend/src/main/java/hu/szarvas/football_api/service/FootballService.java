package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.response.DefaultResponseDTO;
import hu.szarvas.football_api.model.BetStatus;
import hu.szarvas.football_api.model.Match;
import hu.szarvas.football_api.model.MatchScoreBet;
import hu.szarvas.football_api.model.Status;
import hu.szarvas.football_api.repository.MatchRepository;
import hu.szarvas.football_api.repository.MatchScoreBetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FootballService {
    private final MatchScoreBetRepository matchScoreBetRepository;
    private final MatchRepository matchRepository;
    private final LeaderboardService leaderboardService;

    public ResponseEntity<DefaultResponseDTO> makeMatchScoreBet(MatchScoreBet bet) {
        try {
            Match match = matchRepository.getMatchById(bet.getMatchId());
            if (match == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(DefaultResponseDTO.builder()
                                .success(false)
                                .message("Match not found")
                                .build());
            }
            if (!List.of(Status.SCHEDULED, Status.TIMED).contains(match.getStatus())) {
                return ResponseEntity
                        .status(HttpStatus.PRECONDITION_FAILED)
                        .body(DefaultResponseDTO.builder()
                                .success(false)
                                .message("Match already finished")
                                .build());
            }
            if (matchScoreBetRepository.existsByUserIdAndMatchId(bet.getUserId(), bet.getMatchId())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(DefaultResponseDTO.builder()
                                .success(false)
                                .message("You have already placed a bet for this match")
                                .build());
            }

            matchScoreBetRepository.save(bet);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(DefaultResponseDTO.builder()
                            .success(true)
                            .message("Bet made successfully")
                            .build());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DefaultResponseDTO.builder()
                        .success(false)
                        .message(e.getMessage())
                        .build());
        }
    }

    public void updateBetStatuses() {
        try {
            List<MatchScoreBet> activeBets = matchScoreBetRepository.findByStatus(BetStatus.LIVE);

            for (MatchScoreBet bet : activeBets) {
                Match match = matchRepository.getMatchById(bet.getMatchId());

                if (!Status.FINISHED.equals(match.getStatus())) {
                    continue;
                }

                bet.setStatus(BetStatus.FINISHED);
                matchScoreBetRepository.save(bet);

                leaderboardService.processFinishedBet(bet, match);
            }
        } catch (Exception e) {
            log.error("Error updating bet statuses: {}", e.getMessage(), e);
        }
    }
}
