package hu.szarvas.backend.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AreaDTO {
    private int id;
    private String name;
    private String code;
    private String flag;
}
