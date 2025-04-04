package hu.szarvas.football_api.dto.request;

import lombok.Data;

@Data
public class WinnerExternalDTO {
    Integer id;
    String name;
    String shortName;
    String tla;
    String crest;
    String address;
    String website;
    Integer founded;
    String clubColors;
    String venue;
    String lastUpdated;
}
