package hu.szarvas.football_api.dto.request;

import lombok.Data;

@Data
public class GoalDTO {
    private Integer home;
    private Integer away;
}
