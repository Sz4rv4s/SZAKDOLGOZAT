package hu.szarvas.backend.mapper.request;

import hu.szarvas.backend.dto.request.CurrentSeasonDTO;
import hu.szarvas.backend.model.Season;
import org.springframework.stereotype.Component;

@Component
public class SeasonMapper {
    public static Season toSeason(CurrentSeasonDTO currentSeasonDTO) {
        return Season.builder()
                .id(currentSeasonDTO.getId())
                .startDate(currentSeasonDTO.getStartDate())
                .endDate(currentSeasonDTO.getEndDate())
                .currentMatchday(currentSeasonDTO.getCurrentMatchday() != null ? currentSeasonDTO.getCurrentMatchday() : null)
                .winner(currentSeasonDTO.getWinner() != null ? currentSeasonDTO.getWinner().getName() : null)
                .build();
    }
}
