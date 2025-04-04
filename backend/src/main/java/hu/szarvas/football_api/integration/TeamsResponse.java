package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.CurrentSeasonExternalDTO;
import hu.szarvas.football_api.dto.request.TeamExternalDTO;
import lombok.Data;

import java.util.List;

@Data
public class TeamsResponse {
    Integer count;
    Object filters;
    Object competition;
    CurrentSeasonExternalDTO season;
    List<TeamExternalDTO> teams;
}
