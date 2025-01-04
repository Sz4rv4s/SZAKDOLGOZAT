package hu.szarvas.data_collector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "countries")
public class Country {

    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("country_logo")
    private String countryLogo;
}