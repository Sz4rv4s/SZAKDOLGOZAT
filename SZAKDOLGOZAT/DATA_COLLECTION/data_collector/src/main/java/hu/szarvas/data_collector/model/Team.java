package hu.szarvas.data_collector.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "teams")
public class Team {
    @Id
    private String id;
    private String teamKey;
    private String teamName;
    private String teamCountry;
    private String teamFounded;
    private String teamBadge;
    private Venue venue;
    private List<Player> players;
    private List<Coach> coaches;
}
