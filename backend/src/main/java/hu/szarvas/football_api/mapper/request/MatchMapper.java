package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.dto.request.MatchExternalDTO;
import hu.szarvas.football_api.model.Match;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MatchMapper {
    public static Match toMatch(MatchExternalDTO matchExternalDTO) {
        return Match.builder()
                .id(matchExternalDTO.getId())
                .competitionId(matchExternalDTO.getCompetition() != null ? matchExternalDTO.getCompetition().getId() : null)
                .seasonId(matchExternalDTO.getSeason() != null ? matchExternalDTO.getSeason().getId() : null)
                .utcDate(Instant.parse(matchExternalDTO.getUtcDate()))
                .status(matchExternalDTO.getStatus())
                .matchday(matchExternalDTO.getMatchday())
                .lastUpdated(Instant.parse(matchExternalDTO.getLastUpdated()))
                .homeTeamId(matchExternalDTO.getHomeTeam() != null ? matchExternalDTO.getHomeTeam().getId() : null)
                .awayTeamId(matchExternalDTO.getAwayTeam() != null ? matchExternalDTO.getAwayTeam().getId() : null)
                .winner(matchExternalDTO.getScore().getWinner())
                .homeGoals(matchExternalDTO.getScore() != null ?
                        matchExternalDTO.getScore().getFullTime() != null ?
                                matchExternalDTO.getScore().getFullTime().getHome() : null : null)
                .awayGoals(matchExternalDTO.getScore() != null ?
                        matchExternalDTO.getScore().getFullTime() != null ?
                                matchExternalDTO.getScore().getFullTime().getAway() : null : null)
                .build();
    }
}
