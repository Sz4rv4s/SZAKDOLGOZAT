package hu.szarvas.backend.service;

import hu.szarvas.backend.integration.external.AreasResponse;
import hu.szarvas.backend.model.Area;
import hu.szarvas.backend.repository.AreaRepository;
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
                log.debug("Raw API response: {}", response);
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
                    throw e;
                }
            } else {
                log.error("API response was null");
            }
        } catch (Exception e) {
            log.error("Error in fetchAndSaveAreas", e);
            throw e;
        }
    }
}
