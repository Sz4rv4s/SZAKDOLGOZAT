package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matches")
public class Match {
    @Id
    private Integer id;
    private Integer competitionId;
    private Integer seasonId;
    private Instant utcDate;
    private Status status;
    private Integer matchday;
    private Instant lastUpdated;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Winner winner;
    private Integer homeGoals;
    private Integer awayGoals;
}
