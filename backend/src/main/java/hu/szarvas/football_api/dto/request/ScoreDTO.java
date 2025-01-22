package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Winner;
import lombok.Data;

@Data
public class ScoreDTO {
    private Winner winner;
    private String duration;
    private GoalDTO fullTime;
    private GoalDTO halfTime;
}
