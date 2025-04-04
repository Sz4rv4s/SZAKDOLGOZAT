package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.request.UpdateBetDTO;
import hu.szarvas.football_api.dto.response.*;
import hu.szarvas.football_api.mapper.response.MatchResponseMapper;
import hu.szarvas.football_api.mapper.response.MatchScoreBetMapper;
import hu.szarvas.football_api.model.*;
import hu.szarvas.football_api.repository.CompetitionRepository;
import hu.szarvas.football_api.repository.MatchRepository;
import hu.szarvas.football_api.repository.MatchScoreBetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballServiceTest {

    @Mock
    private MatchScoreBetRepository matchScoreBetRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private MatchResponseMapper matchResponseMapper;

    @Mock
    private MatchScoreBetMapper matchScoreBetMapper;

    @InjectMocks
    private FootballService footballService;

    private Match testMatch;
    private MatchScoreBet testBet;
    private Competition testCompetition;

    @BeforeEach
    void setUp() {
        testMatch = new Match();
        testMatch.setId(1);
        testMatch.setStatus(Status.SCHEDULED);

        testBet = new MatchScoreBet();
        testBet.setMatchId(1);
        testBet.setUserId(1);
        testBet.setStatus(BetStatus.LIVE);

        testCompetition = new Competition();
        testCompetition.setId(1);
    }

    @Test
    void makeMatchScoreBet_success() {
        when(matchRepository.getMatchById(1)).thenReturn(testMatch);
        when(matchScoreBetRepository.existsByUserIdAndMatchId(1, 1)).thenReturn(false);
        when(matchScoreBetRepository.save(any(MatchScoreBet.class))).thenReturn(testBet);

        ResponseEntity<DefaultResponseDTO> response = footballService.makeMatchScoreBet(testBet);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Bet made successfully for match: 1", response.getBody().getMessage());
        verify(matchScoreBetRepository).save(testBet);
    }

    @Test
    void makeMatchScoreBet_matchNotFound() {
        when(matchRepository.getMatchById(1)).thenReturn(null);

        ResponseEntity<DefaultResponseDTO> response = footballService.makeMatchScoreBet(testBet);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Match not found: 1", response.getBody().getMessage());
        verify(matchScoreBetRepository, never()).save(any());
    }

    @Test
    void updateBetStatuses_success() {
        testMatch.setStatus(Status.FINISHED);
        when(matchScoreBetRepository.findByStatus(BetStatus.LIVE)).thenReturn(List.of(testBet));
        when(matchRepository.getMatchById(1)).thenReturn(testMatch);
        when(matchScoreBetRepository.save(testBet)).thenReturn(testBet);

        footballService.updateBetStatuses();

        verify(matchScoreBetRepository).save(testBet);
        verify(leaderboardService).processFinishedBet(testBet, testMatch);
        assertEquals(BetStatus.FINISHED, testBet.getStatus());
    }

    @Test
    void getLeagues_success() {
        when(competitionRepository.findByType(CompetitionType.LEAGUE)).thenReturn(List.of(testCompetition));

        ResponseEntity<CompetitionResponseDTO> response = footballService.getLeagues();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Leagues found", response.getBody().getMessage());
        assertEquals(List.of(testCompetition), response.getBody().getCompetitions());
    }

    @Test
    void getUpcomingMatchesForLeague_success() {
        when(matchRepository.getMatchByCompetitionIdAndStatusInAndUtcDateBetween(anyInt(), anyList(), any(), any()))
                .thenReturn(List.of(testMatch));
        MatchDTO matchDTO = new MatchDTO();
        when(matchResponseMapper.toMatchDTO(testMatch)).thenReturn(matchDTO);

        ResponseEntity<MatchResponseDTO> response = footballService.getUpcomingMatchesForLeague(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Matches found for league 1", response.getBody().getMessage());
        assertEquals(List.of(matchDTO), response.getBody().getMatch());
    }

    @Test
    void getMatchScoreBetsForUser_success() {
        when(matchScoreBetRepository.getMatchScoreBetByUserId(1)).thenReturn(List.of(testBet));
        MatchScoreBetDTO betDTO = new MatchScoreBetDTO();
        when(matchScoreBetMapper.toMatchScoreBetDTO(testBet)).thenReturn(betDTO);

        ResponseEntity<MatchScoreBetResponseDTO> response = footballService.getMatchScoreBetsForUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Bets found for user 1", response.getBody().getMessage());
        assertEquals(List.of(betDTO), response.getBody().getMatchScoreBet());
    }

    @Test
    void getMatchScoreBet_found() {
        when(matchScoreBetRepository.findByUserIdAndMatchId(1, 1)).thenReturn(Optional.of(testBet));
        MatchScoreBetDTO betDTO = new MatchScoreBetDTO();
        when(matchScoreBetMapper.toMatchScoreBetDTO(testBet)).thenReturn(betDTO);

        ResponseEntity<?> response = footballService.getMatchScoreBet(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        MatchScoreBetResponseDTO body = (MatchScoreBetResponseDTO) response.getBody();
        assertTrue(Objects.requireNonNull(body).isSuccess());
        assertEquals("Bet found for user 1", body.getMessage());
        assertEquals(List.of(betDTO), body.getMatchScoreBet());
    }

    @Test
    void getMatchScoreBet_notFound() {
        when(matchScoreBetRepository.findByUserIdAndMatchId(1, 1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = footballService.getMatchScoreBet(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        DefaultResponseDTO body = (DefaultResponseDTO) response.getBody();
        assertTrue(Objects.requireNonNull(body).isSuccess());
        assertEquals("Bet not found for user 1", body.getMessage());
    }

    @Test
    void updateMatchScoreBet_success() {
        UpdateBetDTO updateBetDTO = new UpdateBetDTO(1, 2, 1);
        when(matchScoreBetRepository.findByUserIdAndMatchId(1, 1)).thenReturn(Optional.of(testBet));
        when(matchRepository.getMatchById(1)).thenReturn(testMatch);
        when(matchScoreBetRepository.save(any(MatchScoreBet.class))).thenReturn(testBet);

        ResponseEntity<DefaultResponseDTO> response = footballService.updateMatchScoreBet(1, updateBetDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Bet updated successfully for match: 1", response.getBody().getMessage());
        verify(matchScoreBetRepository).save(testBet);
    }

    @Test
    void cancelMatchScoreBet_success() {
        when(matchScoreBetRepository.findByUserIdAndMatchId(1, 1)).thenReturn(Optional.of(testBet));
        when(matchRepository.getMatchById(1)).thenReturn(testMatch);

        ResponseEntity<DefaultResponseDTO> response = footballService.cancelMatchScoreBet(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Bet cancelled successfully for match: 1", response.getBody().getMessage());
        verify(matchScoreBetRepository).delete(testBet);
    }

    @Test
    void cancelMatchScoreBet_noBetFound() {
        when(matchScoreBetRepository.findByUserIdAndMatchId(1, 1)).thenReturn(Optional.empty());

        ResponseEntity<DefaultResponseDTO> response = footballService.cancelMatchScoreBet(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("No bet found to cancel for match: 1", response.getBody().getMessage());
        verify(matchScoreBetRepository, never()).delete(any());
    }
}