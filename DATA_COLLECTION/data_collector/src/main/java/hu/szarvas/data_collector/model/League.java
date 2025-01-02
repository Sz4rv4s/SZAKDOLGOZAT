package hu.szarvas.data_collector.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "leagues")
public class League {
    @Id
    private ObjectId leagueId;
    private String countryId;
    private String countryName;
    private String leagueName;
    private String leagueSeason;
    private String leagueLogo;
    private String countryLogo;
}