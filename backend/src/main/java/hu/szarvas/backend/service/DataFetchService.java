package hu.szarvas.backend.service;

import hu.szarvas.backend.dto.request.CompetitionDTO;
import hu.szarvas.backend.dto.request.MatchDTO;
import hu.szarvas.backend.dto.request.TeamDTO;
import hu.szarvas.backend.integration.external.AreasResponse;
import hu.szarvas.backend.integration.external.CompetitionsResponse;
import hu.szarvas.backend.integration.external.MatchesResponse;
import hu.szarvas.backend.integration.external.TeamsResponse;
import hu.szarvas.backend.mapper.request.CompetitionMapper;
import hu.szarvas.backend.mapper.request.MatchMapper;
import hu.szarvas.backend.mapper.request.TeamMapper;
import hu.szarvas.backend.model.*;
import hu.szarvas.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static hu.szarvas.backend.mapper.request.SeasonMapper.toSeason;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataFetchService {
    private final AreaRepository areaRepository;
    private final CompetitionRepository competitionRepository;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final WebClient webClient;

    private static final TierPlan TIER = TierPlan.TIER_ONE;

    public void fetchAndSaveAreas() {
        try {
            log.debug("Starting area fetch from API");

            AreasResponse response = webClient
                    .get()
                    .uri("/areas")
                    .retrieve()
                    .bodyToMono(AreasResponse.class)
                    .block();

            if (response != null) {
                log.debug("Raw API Area response: {}", response);
                List<Area> areas = response.getAreas();

                if (areas == null || areas.isEmpty()) {
                    log.error("Areas list is null or empty");
                    return;
                }

                log.debug("First area to be saved: {}", areas.getFirst());
                log.debug("Total areas to be saved: {}", areas.size());

                try {
                    List<Area> savedAreas = areaRepository.saveAll(areas);
                    log.info("Successfully saved {} areas", savedAreas.size());
                    log.debug("First saved area: {}", savedAreas.getFirst());
                } catch (Exception e) {
                    log.error("Error during MongoDB save operation", e);
                }
            } else {
                log.error("Area API response was null");
            }
        } catch (Exception e) {
            log.error("Error in fetchAndSaveAreas", e);
            throw e;
        }
    }

    private List<Integer> getIdsForPlanAndType() {
        return competitionRepository.findByPlanAndType(TIER, CompetitionType.LEAGUE)
                .stream()
                .map(Competition::getId)
                .collect(Collectors.toList());
    }

    private void saveSeasons(List<CompetitionDTO> competitions) {
        List<Season> seasons = competitions.stream()
                .filter(comp -> comp.getCurrentSeason() != null)
                .map(comp -> toSeason(comp.getCurrentSeason()))
                .toList();

        if (!seasons.isEmpty()) {
            try {
                List<Season> savedSeasons = seasonRepository.saveAll(seasons);
                log.info("Successfully saved {} seasons", savedSeasons.size());
                log.debug("First saved season: {}", savedSeasons.getFirst());
            } catch (Exception e) {
                log.error("Error during MongoDB season save operation", e);
            }
        }
    }

    private void saveCompetitions(List<CompetitionDTO> competitionDTOs) {
        List<Competition> competitions = competitionDTOs.stream()
                .map(CompetitionMapper::toCompetition)
                .toList();

        if (!competitions.isEmpty()) {
            try {
                List<Competition> savedCompetitions = competitionRepository.saveAll(competitions);
                log.info("Successfully saved {} competitions", savedCompetitions.size());
                log.debug("First saved competition: {}", savedCompetitions.getFirst());
            } catch (Exception e) {
                log.error("Error during MongoDB competition save operation", e);
            }
        }
    }

    public void fetchAndSaveCompetitionsWithSeasons() {
        try {
            log.debug("Starting competitions fetch from API");

            CompetitionsResponse response = webClient
                    .get()
                    .uri("/competitions")
                    .retrieve()
                    .bodyToMono(CompetitionsResponse.class)
                    .block();

            if (response != null) {
                saveSeasons(response.getCompetitions());

                saveCompetitions(response.getCompetitions());
            } else {
                log.error("API response was null");
            }
        } catch (Exception e) {
            log.error("Error in fetchAndSaveCompetitionsWithSeasons", e);
            throw e;
        }
    }

    private void saveTeam(Integer competitionId, TeamsResponse teamsResponse) {
        try {
            log.debug("Starting team fetch from API for league {}", competitionId);

            List<TeamDTO> teamsDTO = teamsResponse.getTeams();
            Integer seasonId = teamsResponse.getSeason().getId();

            List<Team> teams;
            teams = teamsDTO.stream()
                    .map(teamDTO -> TeamMapper.toTeam(teamDTO, competitionId, seasonId))
                    .toList();

            List<Team> savedTeams = teamRepository.saveAll(teams);
            log.info("Successfully saved {} teams", savedTeams.size());
            log.debug("First saved team: {}", savedTeams.getFirst());

        } catch (NoSuchElementException e) {
            log.error("Teams not found for league {}", competitionId);
        } catch (Exception e) {
            log.error("Error during MongoDB team save operation", e);
        }
    }

    private void savePlayers(TeamsResponse teamsResponse) {
        try {
            log.debug("Saving player");
            List<TeamDTO> teamsDTO = teamsResponse.getTeams();
            List<Player> players;
            players = teamsDTO.stream()
                    .map(TeamDTO::getSquad)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            List<Player> savedPlayers = playerRepository.saveAll(players);
            log.info("Successfully saved {} players", savedPlayers.size());
            log.debug("First saved player: {}", savedPlayers.getFirst());
        } catch (NoSuchElementException e) {
            log.debug("Players not found");
        } catch (Exception e) {
            log.debug("Error during MongoDB player save operation", e);
        }
    }

    public void fetchAndSaveTeamsWithPlayers() {
        List<Integer> competitionIds = getIdsForPlanAndType();

        try {
            log.debug("Starting teams fetch from API for {} leagues", competitionIds.size());

            competitionIds.forEach(competitionId -> {
                try {
                    TeamsResponse response = webClient
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/competitions/{competitionId}/teams")
                                    .build(competitionId))
                            .retrieve()
                            .bodyToMono(TeamsResponse.class)
                            .block();

                    if (response != null) {
                        saveTeam(competitionId, response);
                        savePlayers(response);
                    } else {
                        log.warn("Null response received for teams with players for competition ID: {}", competitionId);
                    }
                } catch (Exception e) {
                    log.error("Error during MongoDB team save operation", e);
                }
            });

            log.info("Completed processing teams with players for all competition IDs");

        } catch (Exception e) {
            log.error("Error in fetchAndSaveTeamsWithPlayers", e);
            throw e;
        }
    }

    private void saveMatches(MatchesResponse matchesResponse) {
        try {
            log.debug("Saving match");
            List<MatchDTO> matchesDTO = matchesResponse.getMatches();
            List<Match> matches;
            matches = matchesDTO.stream()
                    .map(MatchMapper::toMatch)
                    .toList();

            List<Match> savedMatches = matchRepository.saveAll(matches);
            log.info("Successfully saved {} matches", savedMatches.size());
            log.debug("First saved match: {}", savedMatches.getFirst());
        } catch (NoSuchElementException e) {
            log.debug("Matches not found");
        } catch (Exception e) {
            log.error("Error during MongoDB matches operation", e);
        }
    }

    public void fetchAndSaveMatches() {
        List<Integer> competitionIds = getIdsForPlanAndType();

        try {
            log.debug("Starting matches fetch from API for {} leagues", competitionIds.size());

            competitionIds.forEach(competitionId -> {
                try {
                    MatchesResponse response = webClient
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/competitions/{competitionId}/matches")
                                    .build(competitionId))
                            .retrieve()
                            .bodyToMono(MatchesResponse.class)
                            .block();
                    if (response != null) {
                        saveMatches(response);
                    } else {
                        log.warn("Null response received for matches for competition ID: {}", competitionId);
                    }

                    log.info("Completed processing matches for all competition IDs");

                } catch (Exception e) {
                    log.error("Error during MongoDB matches fetch operation", e);
                }
            });
        } catch (Exception e) {
            log.error("Error in fetchAndSaveMatches", e);
            throw e;
        }
    }
}
