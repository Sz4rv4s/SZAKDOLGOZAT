package hu.szarvas.backend.integration;

import hu.szarvas.backend.dto.request.CompetitionDTO;
import lombok.Data;

import java.util.List;

@Data
public class CompetitionsResponse {
    private int count;
    private Object filters;
    private List<CompetitionDTO> competitions;
}
