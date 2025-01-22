package hu.szarvas.football_api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionDTO {
    private int id;
    private AreaDTO area;
    private String name;
    private String code;
    private String type;
    private String emblem;
    private String plan;
    private CurrentSeasonDTO currentSeason;
    private Integer numberOfAvailableSeasons;
    private String lastUpdated;
}
