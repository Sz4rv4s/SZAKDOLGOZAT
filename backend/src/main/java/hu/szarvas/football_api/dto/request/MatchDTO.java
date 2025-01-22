package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Status;
import lombok.Data;

@Data
public class MatchDTO {
    private AreaDTO area;
    private CompetitionDTO competition;
    private CurrentSeasonDTO season;
    private Integer id;
    private String utcDate;
    private Status status;
    private Integer matchday;
    private String stage;
    private String group;
    private String lastUpdated;
    private TeamDTO homeTeam;
    private TeamDTO awayTeam;
    private ScoreDTO score;
    private Object odds;
    private Object referees;
}
