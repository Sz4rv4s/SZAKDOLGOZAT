package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(value = "competitions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competition {
    @Id
    private Integer id;
    private Integer areaId;
    private String name;
    private String code;
    private CompetitionType type;
    private String emblem;
    private TierPlan plan;
    private Integer currentSeasonId;
    private Integer numberOfAvailableSeasons;
    private Instant lastUpdated;
}
