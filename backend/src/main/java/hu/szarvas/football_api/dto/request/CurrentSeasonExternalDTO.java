package hu.szarvas.football_api.dto.request;

import lombok.Data;

@Data
public class CurrentSeasonExternalDTO {
    private Integer id;
    private String startDate;
    private String endDate;
    private Integer currentMatchday;
    private WinnerExternalDTO winner;
}
