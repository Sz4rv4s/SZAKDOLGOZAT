package hu.szarvas.football_api.integration;

import hu.szarvas.football_api.model.Area;
import lombok.Data;

import java.util.List;

@Data
public class AreasResponse {
    Integer count;
    Object filters;
    List<Area> areas;
}
