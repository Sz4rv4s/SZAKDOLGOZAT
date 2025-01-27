package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.dto.request.CurrentSeasonExternalDTO;
import hu.szarvas.football_api.model.Season;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeasonMapper {
    public static Season toSeason(CurrentSeasonExternalDTO currentSeasonExternalDTO) {
        return Season.builder()
                .id(currentSeasonExternalDTO.getId())
                .startDate(LocalDate.parse(currentSeasonExternalDTO.getStartDate()))
                .endDate(LocalDate.parse(currentSeasonExternalDTO.getEndDate()))
                .currentMatchday(currentSeasonExternalDTO.getCurrentMatchday() != null ? currentSeasonExternalDTO.getCurrentMatchday() : null)
                .winner(currentSeasonExternalDTO.getWinner() != null ? currentSeasonExternalDTO.getWinner().getName() : null)
                .build();
    }
}
