package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.CurrentSeasonExternalDTO;
import hu.szarvas.football_api.dto.request.TeamExternalDTO;
import lombok.Data;

import java.util.List;

@Data
public class TeamsResponse {
    private Integer count;
    private Object filters;
    private Object competition;
    private CurrentSeasonExternalDTO season;
    private List<TeamExternalDTO> teams;
}
