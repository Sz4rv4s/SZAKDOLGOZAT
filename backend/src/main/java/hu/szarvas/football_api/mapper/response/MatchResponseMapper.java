package hu.szarvas.football_api.mapper.response;

import hu.szarvas.football_api.dto.response.MatchDTO;
import hu.szarvas.football_api.model.Competition;
import hu.szarvas.football_api.model.Match;
import hu.szarvas.football_api.model.Team;
import hu.szarvas.football_api.model.Winner;
import hu.szarvas.football_api.repository.CompetitionRepository;
import hu.szarvas.football_api.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchResponseMapper {
    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;

    public MatchDTO toMatchDTO(Match match) {
        String competitionName;
        String homeTeamShortName;
        String awayTeamShortName;
        String homeTeamCrestUrl;
        String awayTeamCrestUrl;
        Integer winningTeamId = null;
        String winningTeamShortName = "";

        try {
            Competition competition = competitionRepository.getCompetitionById(match.getCompetitionId());
            competitionName = competition.getName();
        } catch (Exception e) {
            competitionName = "";
            log.error("Error getting competition name: ", e);
        }

        try {
            Team homeTeam = teamRepository.getTeamById(match.getHomeTeamId());
            Team awayTeam = teamRepository.getTeamById(match.getAwayTeamId());

            if (homeTeam != null) {
                homeTeamShortName = homeTeam.getShortName();
                homeTeamCrestUrl = homeTeam.getCrest();
            } else {
                log.error("Home team not found for ID: {}", match.getHomeTeamId());
                homeTeamShortName = "";
                homeTeamCrestUrl = "";
            }

            if (awayTeam != null) {
                awayTeamShortName = awayTeam.getShortName();
                awayTeamCrestUrl = awayTeam.getCrest();
            } else {
                log.error("Away team not found for ID: {}", match.getAwayTeamId());
                awayTeamShortName = "";
                awayTeamCrestUrl = "";
            }

            if (homeTeam != null && awayTeam != null && match.getWinner() != null) {
                winningTeamId = getWinnerId(homeTeam, awayTeam, match.getWinner());
                winningTeamShortName = getWinnerShortName(homeTeam, awayTeam, match.getWinner());
            }
        } catch (Exception e) {
            log.error("Error getting teams data: ", e);
            homeTeamShortName = "";
            awayTeamShortName = "";
            homeTeamCrestUrl = "";
            awayTeamCrestUrl = "";
        }

        return MatchDTO.builder()
                .id(match.getId())
                .competitionId(match.getCompetitionId())
                .competitionName(competitionName)
                .utcDate(match.getUtcDate())
                .status(match.getStatus())
                .homeTeamId(match.getHomeTeamId())
                .awayTeamId(match.getAwayTeamId())
                .homeTeamShortName(homeTeamShortName)
                .awayTeamShortName(awayTeamShortName)
                .homeTeamCrestUrl(homeTeamCrestUrl)
                .awayTeamCrestUrl(awayTeamCrestUrl)
                .winningTeamId(winningTeamId)
                .winningTeamShortName(winningTeamShortName)
                .homeGoals(match.getHomeGoals())
                .awayGoals(match.getAwayGoals())
                .build();
    }

    private Integer getWinnerId(Team homeTeam, Team awayTeam, Winner winner) {
        return switch (winner) {
            case HOME_TEAM -> homeTeam.getId();
            case AWAY_TEAM -> awayTeam.getId();
            case DRAW -> null;
        };
    }

    private String getWinnerShortName(Team homeTeam, Team awayTeam, Winner winner) {
        return switch (winner) {
            case HOME_TEAM -> homeTeam.getName();
            case AWAY_TEAM -> awayTeam.getName();
            case DRAW -> "DRAW";
        };
    }
}
