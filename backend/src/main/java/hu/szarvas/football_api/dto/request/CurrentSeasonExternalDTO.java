package hu.szarvas.football_api.dto.request;

import lombok.Data;

@Data
public class CurrentSeasonExternalDTO {
    Integer id;
    String startDate;
    String endDate;
    Integer currentMatchday;
    WinnerExternalDTO winner;
}
