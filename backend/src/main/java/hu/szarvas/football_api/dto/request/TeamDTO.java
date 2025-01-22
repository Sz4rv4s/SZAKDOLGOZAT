package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Player;
import lombok.Data;

import java.util.List;

@Data
public class TeamDTO {
    private AreaDTO area;
    private Integer id;
    private String name;
    private String shortName;
    private String tla;
    private String crest;
    private String address;
    private String website;
    private Integer founded;
    private String clubColors;
    private String venue;
    private Object runningCompetitions;
    private Object coach;
    private List<Player> squad;
    private Object staff;
    private String lastUpdated;
}
