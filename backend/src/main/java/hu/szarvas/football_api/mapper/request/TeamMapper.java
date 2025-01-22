package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.dto.request.TeamDTO;
import hu.szarvas.football_api.model.Player;
import hu.szarvas.football_api.model.Team;
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
