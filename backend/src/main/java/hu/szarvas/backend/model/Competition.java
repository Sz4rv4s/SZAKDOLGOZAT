package hu.szarvas.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "competitions")
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
    private String lastUpdated;

}
