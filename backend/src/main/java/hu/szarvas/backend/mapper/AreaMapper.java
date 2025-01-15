package hu.szarvas.backend.mapper;

import hu.szarvas.backend.dto.response.AreaDTO;
import hu.szarvas.backend.model.Area;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {
    public AreaDTO toAreaDTO(Area area) {
        return AreaDTO.builder()
                .name(area.getName())
                .countryCode(area.getCountryCode())
                .flag(area.getFlag())
                .parentArea(area.getParentArea())
                .build();
    }
}
