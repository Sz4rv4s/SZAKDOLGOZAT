package hu.szarvas.football_api.dto.response;

import hu.szarvas.football_api.model.BetStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MatchScoreBetDTO {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer matchId;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamShortName;
    private String awayTeamShortName;
    private Integer homeScoreBet;
    private Integer awayScoreBet;
    private Integer homeScore;
    private Integer awayScore;
    private BetStatus status;
    private Integer points;
    private Instant dateOfBet;
    private Instant dateOfMatch;
}
