package hu.szarvas.football_api.dto.response;

import hu.szarvas.football_api.model.Status;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MatchDTO {
    private Integer id;
    private Integer competitionId;
    private String competitionName;
    private Instant utcDate;
    private Status status;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamShortName;
    private String awayTeamShortName;
    private String homeTeamCrestUrl;
    private String awayTeamCrestUrl;
    private Integer winningTeamId;
    private String winningTeamShortName;
    private Integer homeGoals;
    private Integer awayGoals;
}
