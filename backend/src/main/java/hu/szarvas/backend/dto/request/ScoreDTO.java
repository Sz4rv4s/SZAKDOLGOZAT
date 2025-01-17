package hu.szarvas.backend.dto.request;

import hu.szarvas.backend.model.Winner;
import lombok.Data;

import java.util.Map;

@Data
public class ScoreDTO {
    private Winner winner;
    private String duration;
    private GoalDTO fullTime;
    private GoalDTO halfTime;
}
