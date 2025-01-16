package hu.szarvas.backend.integration.external;

import hu.szarvas.backend.dto.request.CurrentSeasonDTO;
import hu.szarvas.backend.dto.request.TeamDTO;
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
