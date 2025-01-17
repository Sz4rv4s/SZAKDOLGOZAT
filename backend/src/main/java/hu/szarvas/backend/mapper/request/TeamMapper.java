package hu.szarvas.backend.mapper.request;

import hu.szarvas.backend.dto.request.TeamDTO;
import hu.szarvas.backend.model.Player;
import hu.szarvas.backend.model.Team;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeamMapper {
    public static Team toTeam(TeamDTO teamDTO, Integer competitionId, Integer seasonId) {
        return Team.builder()
                .id(teamDTO.getId())
                .competitionId(competitionId)
                .seasonId(seasonId)
                .areaId(teamDTO.getArea().getId())
                .name(teamDTO.getName())
                .shortName(teamDTO.getShortName())
                .tla(teamDTO.getTla())
                .crest(teamDTO.getCrest())
                .playerIds(teamDTO.getSquad()
                        .stream()
                        .map(Player::getId)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
