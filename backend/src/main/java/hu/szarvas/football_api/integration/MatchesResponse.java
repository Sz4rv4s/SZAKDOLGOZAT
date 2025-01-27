package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.MatchExternalDTO;
import lombok.Data;

import java.util.List;

@Data
public class MatchesResponse {
    private Object filter;
    private Object resultSet;
    private Object competition;
    private List<MatchExternalDTO> matches;
}
