package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Winner;
import lombok.Data;

@Data
public class ScoreExternalDTO {
    private Winner winner;
    private String duration;
    private GoalExternalDTO fullTime;
    private GoalExternalDTO halfTime;
}
