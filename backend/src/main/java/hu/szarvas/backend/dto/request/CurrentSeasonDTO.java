package hu.szarvas.backend.dto.request;

import lombok.Data;

@Data
public class CurrentSeasonDTO {
    private Integer id;
    private String startDate;
    private String endDate;
    private Integer currentMatchday;
    private WinnerDTO winner;
}
