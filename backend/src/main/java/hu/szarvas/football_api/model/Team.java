package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(value = "teams")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    private Integer id;
    private Integer competitionId;
    private Integer seasonId;
    private Integer areaId;
    private String name;
    private String shortName;
    private String tla;
    private String crest;
    private List<Integer> playerIds;
}
