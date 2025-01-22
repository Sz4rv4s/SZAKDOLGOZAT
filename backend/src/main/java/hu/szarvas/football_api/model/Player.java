package hu.szarvas.football_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "players")
public class Player {
    @Id
    private Integer id;
    private String name;
    private String position;
    private String dateOfBirth;
    private String nationality;
}
