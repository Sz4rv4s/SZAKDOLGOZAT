package hu.szarvas.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(value = "teams")
public class Team {
    @Id
    private Integer id;
    private Integer areaId;
    private String name;
    private String shortName;
    private String tla;
    private String crest;
    private String address;
    private String website;
    private Integer founded;
    private String clubColors;
    private String venue;
    private List<Player> squad;
    private String lastUpdated;
}
