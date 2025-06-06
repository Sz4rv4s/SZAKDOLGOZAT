package hu.szarvas.football_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "areas")
public class Area {
    @Id
    private Integer id;
    private String name;
    private String countryCode;
    private String flag;
    private Integer parentAreaId;
    private String parentArea;
}
