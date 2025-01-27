package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.dto.request.CompetitionExternalDTO;
import lombok.Data;

import java.util.List;

@Data
public class CompetitionsResponse {
    private Integer count;
    private Object filters;
    private List<CompetitionExternalDTO> competitions;
}
