package hu.szarvas.football_api.dto.response;

import hu.szarvas.football_api.model.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchScoreBetDTO {
    Integer id;
    Integer userId;
    String userName;
    Integer matchId;
    Integer homeTeamId;
    Integer awayTeamId;
    String homeTeamShortName;
    String awayTeamShortName;
    Integer homeScoreBet;
    Integer awayScoreBet;
    Integer homeScore;
    Integer awayScore;
    BetStatus status;
    Integer points;
    Instant dateOfBet;
    Instant dateOfMatch;
}
