package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.dto.request.CompetitionDTO;
import hu.szarvas.football_api.model.Competition;
import org.springframework.stereotype.Component;

@Component
public class CompetitionMapper {
    public static Competition toCompetition(CompetitionDTO competitionDTO) {
        return Competition.builder()
                .id(competitionDTO.getId())
                .areaId(competitionDTO.getArea().getId())
                .name(competitionDTO.getName())
                .code(competitionDTO.getCode())
                .type(CompetitionTypeMapper.toCompetitionType(competitionDTO.getType()))
                .emblem(competitionDTO.getEmblem())
                .plan(TierPlanMapper.toTierPlan(competitionDTO.getPlan()))
                .currentSeasonId(competitionDTO.getCurrentSeason() != null ? competitionDTO.getCurrentSeason().getId() : null)
                .numberOfAvailableSeasons(competitionDTO.getNumberOfAvailableSeasons())
                .lastUpdated(competitionDTO.getLastUpdated())
                .build();
    }
}
