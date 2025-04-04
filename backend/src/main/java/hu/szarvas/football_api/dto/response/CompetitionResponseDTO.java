package hu.szarvas.football_api.dto.response;

import hu.szarvas.football_api.model.Competition;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompetitionResponseDTO {
    boolean success;
    String message;
    List<Competition> competitions;
}
