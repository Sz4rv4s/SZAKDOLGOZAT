package hu.szarvas.football_api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionExternalDTO {
    Integer id;
    AreaExternalDTO area;
    String name;
    String code;
    String type;
    String emblem;
    String plan;
    CurrentSeasonExternalDTO currentSeason;
    Integer numberOfAvailableSeasons;
    String lastUpdated;
}
