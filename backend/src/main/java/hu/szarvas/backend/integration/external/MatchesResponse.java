package hu.szarvas.backend.integration.external;

import hu.szarvas.backend.dto.request.MatchDTO;
import lombok.Data;

import java.util.List;

@Data
public class MatchesResponse {
    private Object filter;
    private Object resultSet;
    private Object competition;
    private List<MatchDTO> matches;
}
