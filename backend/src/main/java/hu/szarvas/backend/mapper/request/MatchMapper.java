package hu.szarvas.backend.mapper.request;

import hu.szarvas.backend.dto.request.MatchDTO;
import hu.szarvas.backend.model.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {
    public static Match toMatch(MatchDTO matchDTO) {
        return Match.builder()
                .id(matchDTO.getId())
                .competitionId(matchDTO.getCompetition().getId())
                .seasonId(matchDTO.getSeason().getId())
                .utcDate(matchDTO.getUtcDate())
                .status(matchDTO.getStatus())
                .matchday(matchDTO.getMatchday())
                .lastUpdated(matchDTO.getLastUpdated())
                .homeTeamId(matchDTO.getHomeTeam().getId())
                .awayTeamId(matchDTO.getAwayTeam().getId())
                .winner(matchDTO.getScore().getWinner())
                .homeGoals(matchDTO.getScore().getFullTime().getHome())
                .awayGoals(matchDTO.getScore().getFullTime().getAway())
                .build();
    }
}
