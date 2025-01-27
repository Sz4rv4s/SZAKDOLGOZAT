package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.dto.request.CompetitionExternalDTO;
import hu.szarvas.football_api.model.Competition;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CompetitionMapper {
    public static Competition toCompetition(CompetitionExternalDTO competitionExternalDTO) {
        return Competition.builder()
                .id(competitionExternalDTO.getId())
                .areaId(competitionExternalDTO.getArea() != null ? competitionExternalDTO.getArea().getId() : null)
                .name(competitionExternalDTO.getName())
                .code(competitionExternalDTO.getCode())
                .type(CompetitionTypeMapper.toCompetitionType(competitionExternalDTO.getType()))
                .emblem(competitionExternalDTO.getEmblem())
                .plan(TierPlanMapper.toTierPlan(competitionExternalDTO.getPlan()))
                .currentSeasonId(competitionExternalDTO.getCurrentSeason() != null ? competitionExternalDTO.getCurrentSeason().getId() : null)
                .numberOfAvailableSeasons(competitionExternalDTO.getNumberOfAvailableSeasons())
                .lastUpdated(Instant.parse(competitionExternalDTO.getLastUpdated()))
                .build();
    }
}
