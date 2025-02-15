package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(value = "seasons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer currentMatchday;
    private String winner;
}
