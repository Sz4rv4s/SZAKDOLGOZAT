package hu.szarvas.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "seasons")
public class Season {
    @Id
    private Integer id;
    private String startDate;
    private String endDate;
    private Integer currentMatchday;
    private String winner;
}
