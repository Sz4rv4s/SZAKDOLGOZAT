package hu.szarvas.football_api.dto.request;

import lombok.Data;

@Data
public class WinnerExternalDTO {
    private Integer id;
    private String name;
    private String shortName;
    private String tla;
    private String crest;
    private String address;
    private String website;
    private Integer founded;
    private String clubColors;
    private String venue;
    private String lastUpdated;
}
