package hu.szarvas.backend.service;

import hu.szarvas.backend.dto.request.CompetitionDTO;
import hu.szarvas.backend.integration.external.AreasResponse;
import hu.szarvas.backend.integration.external.CompetitionsResponse;
import hu.szarvas.backend.mapper.request.CompetitionMapper;
import hu.szarvas.backend.model.Area;
import hu.szarvas.backend.model.Competition;
import hu.szarvas.backend.model.Season;
import hu.szarvas.backend.repository.AreaRepository;
import hu.szarvas.backend.repository.CompetitionRepository;
import hu.szarvas.backend.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static hu.szarvas.backend.mapper.request.SeasonMapper.toSeason;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataFetchService {
    private final AreaRepository areaRepository;
    private final CompetitionRepository competitionRepository;
    private final SeasonRepository seasonRepository;
    private final WebClient webClient;

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
}
