package hu.szarvas.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "seasons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    private Integer id;
    private String startDate;
    private String endDate;
    private Integer currentMatchday;
    private String winner;
}
