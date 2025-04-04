package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Winner;
import lombok.Data;

@Data
public class ScoreExternalDTO {
    Winner winner;
    String duration;
    GoalExternalDTO fullTime;
    GoalExternalDTO halfTime;
}
