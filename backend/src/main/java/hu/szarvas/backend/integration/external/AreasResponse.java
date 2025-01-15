package hu.szarvas.backend.integration.external;

import hu.szarvas.backend.model.Area;
import lombok.Data;

import java.util.List;

@Data
public class AreasResponse {
    private int count;
    private Object filters;
    private List<Area> areas;
}
