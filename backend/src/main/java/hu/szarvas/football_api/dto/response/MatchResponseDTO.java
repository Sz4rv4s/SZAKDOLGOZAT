package hu.szarvas.football_api.dto.response;

import hu.szarvas.football_api.model.Match;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchResponseDTO {
    boolean success;
    String message;
    List<Match> match;
}
