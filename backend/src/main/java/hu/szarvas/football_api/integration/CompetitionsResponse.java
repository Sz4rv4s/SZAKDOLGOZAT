package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.CompetitionDTO;
import lombok.Data;

import java.util.List;

@Data
public class CompetitionsResponse {
    private int count;
    private Object filters;
    private List<CompetitionDTO> competitions;
}
