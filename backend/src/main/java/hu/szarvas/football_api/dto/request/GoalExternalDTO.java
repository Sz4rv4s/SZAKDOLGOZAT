package hu.szarvas.football_api.dto.request;

import lombok.Data;

@Data
public class GoalExternalDTO {
    private Integer home;
    private Integer away;
}
