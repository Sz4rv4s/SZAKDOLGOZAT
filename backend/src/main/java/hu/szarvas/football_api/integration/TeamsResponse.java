package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.CurrentSeasonDTO;
import hu.szarvas.football_api.dto.request.TeamDTO;
import lombok.Data;

import java.util.List;

@Data
public class TeamsResponse {
    private int count;
    private Object filters;
    private Object competition;
    private CurrentSeasonDTO season;
    private List<TeamDTO> teams;
}
