package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.dto.request.TeamExternalDTO;
import hu.szarvas.football_api.model.Player;
import hu.szarvas.football_api.model.Team;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeamMapper {
    public static Team toTeam(TeamExternalDTO teamExternalDTO, Integer competitionId, Integer seasonId) {
        return Team.builder()
                .id(teamExternalDTO.getId())
                .competitionId(competitionId)
                .seasonId(seasonId)
                .areaId(teamExternalDTO.getArea() != null ? teamExternalDTO.getArea().getId() : null)
                .name(teamExternalDTO.getName())
                .shortName(teamExternalDTO.getShortName())
                .tla(teamExternalDTO.getTla())
                .crest(teamExternalDTO.getCrest())
                .playerIds(teamExternalDTO.getSquad()
                        .stream()
                        .map(Player::getId)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
