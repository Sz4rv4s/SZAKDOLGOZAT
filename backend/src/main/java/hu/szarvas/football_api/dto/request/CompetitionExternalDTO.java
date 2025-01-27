package hu.szarvas.football_api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionExternalDTO {
    private Integer id;
    private AreaExternalDTO area;
    private String name;
    private String code;
    private String type;
    private String emblem;
    private String plan;
    private CurrentSeasonExternalDTO currentSeason;
    private Integer numberOfAvailableSeasons;
    private String lastUpdated;
}
