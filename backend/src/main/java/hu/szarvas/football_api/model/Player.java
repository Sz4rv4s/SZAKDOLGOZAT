package hu.szarvas.football_api.model;

import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(value = "players")
public class Player {
    @Id
    private Integer id;
    private String name;
    private String position;
    private LocalDate dateOfBirth;
    private String nationality;
}
