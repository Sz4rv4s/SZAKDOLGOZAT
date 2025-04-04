package hu.szarvas.football_api.dto.request;

import hu.szarvas.football_api.model.Player;
import lombok.Data;

import java.util.List;

@Data
public class TeamExternalDTO {
    AreaExternalDTO area;
    Integer id;
    String name;
    String shortName;
    String tla;
    String crest;
    String address;
    String website;
    Integer founded;
    String clubColors;
    String venue;
    Object runningCompetitions;
    Object coach;
    List<Player> squad;
    Object staff;
    String lastUpdated;
}
