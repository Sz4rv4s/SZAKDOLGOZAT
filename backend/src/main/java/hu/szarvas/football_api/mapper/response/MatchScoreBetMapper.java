package hu.szarvas.football_api.mapper.response;

import hu.szarvas.football_api.dto.response.MatchScoreBetDTO;
import hu.szarvas.football_api.model.*;
import hu.szarvas.football_api.repository.MatchRepository;
import hu.szarvas.football_api.repository.TeamRepository;
import hu.szarvas.football_api.repository.UserPointsRepository;
import hu.szarvas.football_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchScoreBetMapper {
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final UserPointsRepository userPointsRepository;


    public MatchScoreBetDTO toMatchScoreBetDTO(MatchScoreBet matchScoreBet) {
        String userName;
        Integer homeTeamId;
        Integer awayTeamId;
        String homeTeamShortName;
        String awayTeamShortName;
        Integer homeScore;
        Integer awayScore;
        Integer points;
        Instant matchDate;

        try {
            User user = userRepository.getUserById(matchScoreBet.getUserId());
            userName = user.getUsername();
        } catch (Exception e) {
            userName = "";
            log.error("Error getting username: ", e);
        }

        try {
            Match match = matchRepository.getMatchById(matchScoreBet.getMatchId());
            homeTeamId = match.getHomeTeamId();
            awayTeamId = match.getAwayTeamId();
            homeScore = match.getHomeGoals();
            awayScore = match.getAwayGoals();
            matchDate = match.getUtcDate();
        } catch (Exception e) {
            homeTeamId = null;
            awayTeamId = null;
            homeScore = null;
            awayScore = null;
            matchDate = null;
            log.error("Error getting match: ", e);
        }

        try {
            Team homeTeam = teamRepository.getTeamById(homeTeamId);
            Team awayTeam = teamRepository.getTeamById(awayTeamId);
            homeTeamShortName = homeTeam.getShortName();
            awayTeamShortName = awayTeam.getShortName();
        } catch (Exception e) {
            homeTeamShortName = null;
            awayTeamShortName = null;
            log.error("Error getting team names: ", e);
        }

        try {
            Optional<UserPoints> userPoints = userPointsRepository.getUserPointsByUserIdAndMatchId(matchScoreBet.getUserId(), matchScoreBet.getMatchId());
            points = userPoints.map(UserPoints::getPoints).orElse(null);
        } catch (Exception e) {
            points = null;
            log.error("Error getting points: ", e);
        }

        return MatchScoreBetDTO.builder()
                .id(matchScoreBet.getId())
                .userId(matchScoreBet.getUserId())
                .userName(userName)
                .matchId(matchScoreBet.getMatchId())
                .homeTeamId(homeTeamId)
                .awayTeamId(awayTeamId)
                .homeTeamShortName(homeTeamShortName)
                .awayTeamShortName(awayTeamShortName)
                .homeScoreBet(matchScoreBet.getHomeScoreBet())
                .awayScoreBet(matchScoreBet.getAwayScoreBet())
                .homeScore(homeScore)
                .awayScore(awayScore)
                .status(matchScoreBet.getStatus())
                .points(points)
                .dateOfMatch(matchScoreBet.getDate())
                .dateOfMatch(matchDate)
                .build();
    }
}
