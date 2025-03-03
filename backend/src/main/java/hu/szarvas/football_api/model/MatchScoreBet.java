package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "match_score_bets")
public class MatchScoreBet {
    @Id
    private Integer id;

    @Transient
    public static final String SEQUENCE_NAME = "match_score_bet_sequence";

    private Integer userId;
    private Integer matchId;
    private Integer homeScoreBet;
    private Integer awayScoreBet;
    private BetStatus status;
    private Instant date;

}
