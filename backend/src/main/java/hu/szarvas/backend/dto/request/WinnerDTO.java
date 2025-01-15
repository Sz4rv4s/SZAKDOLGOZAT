package hu.szarvas.backend.dto.request;

import lombok.Data;

@Data
public class WinnerDTO {
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
