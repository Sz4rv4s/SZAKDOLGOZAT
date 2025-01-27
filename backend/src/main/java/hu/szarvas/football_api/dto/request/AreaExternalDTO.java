package hu.szarvas.football_api.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AreaExternalDTO {
    private Integer id;
    private String name;
    private String code;
    private String flag;
}
