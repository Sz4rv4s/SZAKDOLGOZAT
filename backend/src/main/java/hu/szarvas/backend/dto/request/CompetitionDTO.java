package hu.szarvas.backend.dto.request;

import lombok.Data;

@Data
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
