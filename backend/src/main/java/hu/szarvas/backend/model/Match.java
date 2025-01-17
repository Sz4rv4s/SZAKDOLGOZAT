package hu.szarvas.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String utcDate;
    private Status status;
    private Integer matchday;
    private String lastUpdated;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Winner winner;
    private Integer homeGoals;
    private Integer awayGoals;
}
