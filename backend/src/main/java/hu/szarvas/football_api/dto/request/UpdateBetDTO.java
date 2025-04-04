package hu.szarvas.football_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBetDTO {
    Integer matchId;
    Integer homeScoreBet;
    Integer awayScoreBet;

}
