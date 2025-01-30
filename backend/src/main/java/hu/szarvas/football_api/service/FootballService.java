package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.response.*;
import hu.szarvas.football_api.mapper.response.MatchResponseMapper;
import hu.szarvas.football_api.mapper.response.MatchScoreBetMapper;
import hu.szarvas.football_api.model.*;
import hu.szarvas.football_api.repository.CompetitionRepository;
import hu.szarvas.football_api.repository.MatchRepository;
import hu.szarvas.football_api.repository.MatchScoreBetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FootballService {
    private final MatchScoreBetRepository matchScoreBetRepository;
    private final MatchRepository matchRepository;
    private final LeaderboardService leaderboardService;
    private final CompetitionRepository competitionRepository;
    private final MatchResponseMapper matchResponseMapper;
    private final MatchScoreBetMapper matchScoreBetMapper;

    public ResponseEntity<DefaultResponseDTO> makeMatchScoreBet(MatchScoreBet bet) {
        try {
            Match match = matchRepository.getMatchById(bet.getMatchId());
            if (match == null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(DefaultResponseDTO.builder()
                                .success(false)
                                .message("Match not found: " + bet.getMatchId())
                                .build());
            }
            if (!List.of(Status.SCHEDULED, Status.TIMED).contains(match.getStatus())) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(DefaultResponseDTO.builder()
                                .success(false)
                                .message("Match already finished: " + bet.getMatchId())
                                .build());
            }
            if (matchScoreBetRepository.existsByUserIdAndMatchId(bet.getUserId(), bet.getMatchId())) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(DefaultResponseDTO.builder()
                                .success(false)
                                .message("You have already placed a bet for match: " + bet.getMatchId())
                                .build());
            }

            matchScoreBetRepository.save(bet);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(DefaultResponseDTO.builder()
                            .success(true)
                            .message("Bet made successfully for match: " + bet.getMatchId())
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

    public ResponseEntity<CompetitionResponseDTO> getLeagues() {
        try {
            List<Competition> leagues = competitionRepository.findByType(CompetitionType.LEAGUE);
            if (leagues.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(CompetitionResponseDTO.builder()
                                .success(true)
                                .message("No league found")
                                .build());
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CompetitionResponseDTO.builder()
                            .success(true)
                            .message("Leagues found")
                            .competitions(leagues)
                    .build());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CompetitionResponseDTO.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }

    }

    public ResponseEntity<MatchResponseDTO> getUpcomingMatchesForLeague(Integer leagueId) {
        try {
            List<Match> matches = matchRepository.getMatchByCompetitionIdAndStatusInAndUtcDateBetween(
                    leagueId,
                    List.of(Status.SCHEDULED, Status.TIMED),
                    Instant.now(),
                    Instant.now().plus(Duration.ofDays(7))
            );

            if (matches.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(MatchResponseDTO.builder()
                                .success(true)
                                .message("No matches found for league " + leagueId)
                                .build());
            }

            List<MatchDTO> matchesDTO = matches.stream()
                    .map(matchResponseMapper::toMatchDTO)
                    .toList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MatchResponseDTO.builder()
                            .success(true)
                            .message("Matches found for league " + leagueId)
                            .match(matchesDTO)
                            .build());

        } catch (Exception e) {
            log.error("Error retrieving matches for league {}", leagueId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MatchResponseDTO.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    public ResponseEntity<MatchScoreBetResponseDTO> getMatchScoreBetsForUser(Integer userId) {
        try {
            List<MatchScoreBet> matchScoreBets = matchScoreBetRepository.getMatchScoreBetByUserId(userId);

            if (matchScoreBets.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(MatchScoreBetResponseDTO.builder()
                                .success(true)
                                .message("No bet found for user " + userId)
                                .build());
            }

            List<MatchScoreBetDTO> matchScoreBetsDTO = matchScoreBets.stream()
                    .map(matchScoreBetMapper::toMatchScoreBetDTO)
                    .toList();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MatchScoreBetResponseDTO.builder()
                            .success(true)
                            .message("Bets found for user " + userId)
                            .matchScoreBet(matchScoreBetsDTO)
                            .build());

        } catch (Exception e) {
            log.error("Error retrieving match score bets for user {}", userId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MatchScoreBetResponseDTO.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

}
