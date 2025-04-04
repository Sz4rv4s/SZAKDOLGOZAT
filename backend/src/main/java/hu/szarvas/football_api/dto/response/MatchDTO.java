package hu.szarvas.football_api.dto.response;

import hu.szarvas.football_api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    Integer id;
    Integer competitionId;
    String competitionName;
    Instant utcDate;
    Status status;
    Integer homeTeamId;
    Integer awayTeamId;
    String homeTeamShortName;
    String awayTeamShortName;
    String homeTeamCrestUrl;
    String awayTeamCrestUrl;
    Integer winningTeamId;
    String winningTeamShortName;
    Integer homeGoals;
    Integer awayGoals;
}
