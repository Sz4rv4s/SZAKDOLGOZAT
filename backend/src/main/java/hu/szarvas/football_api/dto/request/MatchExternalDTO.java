package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Status;
import lombok.Data;

@Data
public class MatchExternalDTO {
    AreaExternalDTO area;
    CompetitionExternalDTO competition;
    CurrentSeasonExternalDTO season;
    Integer id;
    String utcDate;
    Status status;
    Integer matchday;
    String stage;
    String group;
    String lastUpdated;
    TeamExternalDTO homeTeam;
    TeamExternalDTO awayTeam;
    ScoreExternalDTO score;
    Object odds;
    Object referees;
}
