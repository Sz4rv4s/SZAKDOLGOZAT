package hu.szarvas.football_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchScoreBetResponseDTO {
    boolean success;
    String message;
    List<MatchScoreBetDTO> matchScoreBet;
}
