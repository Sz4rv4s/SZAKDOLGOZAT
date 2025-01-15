package hu.szarvas.backend.service;

import hu.szarvas.backend.dto.request.CompetitionDTO;
import hu.szarvas.backend.integration.external.AreasResponse;
import hu.szarvas.backend.integration.external.CompetitionsResponse;
import hu.szarvas.backend.mapper.request.CompetitionMapper;
import hu.szarvas.backend.model.Area;
import hu.szarvas.backend.model.Competition;
import hu.szarvas.backend.repository.AreaRepository;
import hu.szarvas.backend.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataFetchService {
    private final AreaRepository areaRepository;
    private final CompetitionRepository competitionRepository;
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

    public void fetchAndSaveCompetitions() {
        try {
            log.debug("Starting competition fetch from API");

            CompetitionsResponse response = webClient
                    .get()
                    .uri("/competitions")
                    .retrieve()
                    .bodyToMono(CompetitionsResponse.class)
                    .block();

            if (response != null) {
                log.debug("Raw API Competition response: {}", response);
                List<CompetitionDTO> competitionDTOs = response.getCompetitions();

                if (competitionDTOs == null || competitionDTOs.isEmpty()) {
                    log.error("Competitions list is null or empty");
                    return;
                }

                List<Competition> competitions = competitionDTOs.stream()
                        .map(CompetitionMapper::toCompetition)
                        .toList();

                log.debug("First competition to be saved: {}", competitions.getFirst());
                log.debug("Total competition to be saved: {}", competitions.size());

                try {
                    List<Competition> savedCompetitions = competitionRepository.saveAll(competitions);
                    log.info("Successfully saved {} competitions", savedCompetitions.size());
                    log.debug("First saved competition: {}", savedCompetitions.getFirst());
                } catch (Exception e) {
                    log.error("Error during MongoDB save operation", e);
                }
            } else {
                log.error("Competitions API response was null");
            }
        } catch (Exception e) {
            log.error("Error in fetchAndSaveCompetitions", e);
            throw e;
        }
    }
}
