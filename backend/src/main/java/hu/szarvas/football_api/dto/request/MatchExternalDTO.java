package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Status;
import lombok.Data;

@Data
public class MatchExternalDTO {
    private AreaExternalDTO area;
    private CompetitionExternalDTO competition;
    private CurrentSeasonExternalDTO season;
    private Integer id;
    private String utcDate;
    private Status status;
    private Integer matchday;
    private String stage;
    private String group;
    private String lastUpdated;
    private TeamExternalDTO homeTeam;
    private TeamExternalDTO awayTeam;
    private ScoreExternalDTO score;
    private Object odds;
    private Object referees;
}
