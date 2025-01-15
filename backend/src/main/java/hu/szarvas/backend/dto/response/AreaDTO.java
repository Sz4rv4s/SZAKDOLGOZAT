package hu.szarvas.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AreaDTO {
    private String name;
    private String countryCode;
    private String flag;
    private String parentArea;
}
