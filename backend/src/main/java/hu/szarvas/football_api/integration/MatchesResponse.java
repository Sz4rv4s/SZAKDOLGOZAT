package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.MatchExternalDTO;
import lombok.Data;

import java.util.List;

@Data
public class MatchesResponse {
    Object filter;
    Object resultSet;
    Object competition;
    List<MatchExternalDTO> matches;
}
