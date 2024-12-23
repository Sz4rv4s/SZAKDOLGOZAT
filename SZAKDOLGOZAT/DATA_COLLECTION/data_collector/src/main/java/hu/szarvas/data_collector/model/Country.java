package hu.szarvas.data_collector.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "countries")
public class Country {
    @Id
    private ObjectId id;
    private String countryName;
    private String countryLogo;
}
