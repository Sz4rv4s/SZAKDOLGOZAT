package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_points")
public class UserPoints {

    @Id
    private Integer id;

    @Transient
    public static final String SEQUENCE_NAME = "user_points_sequence";

    private Integer userId;
    private Integer points;
    private Integer matchId;
    private Instant matchDate;
}
