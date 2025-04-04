package hu.szarvas.football_api.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AreaExternalDTO {
    Integer id;
    String name;
    String code;
    String flag;
}
