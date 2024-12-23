package hu.szarvas.data_collector.service;

import hu.szarvas.data_collector.model.Country;
import hu.szarvas.data_collector.model.League;
import hu.szarvas.data_collector.model.Player;
import hu.szarvas.data_collector.model.Team;
import hu.szarvas.data_collector.repository.CountryRepository;
import hu.szarvas.data_collector.repository.LeagueRepository;
import hu.szarvas.data_collector.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveDataCollectorService {
    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    private String countriesApiUrl = apiUrl + "?action=get_countries&APIkey=" + apiKey;
    private String leaguesApiUrl = apiUrl + "?action=get_leagues&APIkey=" + apiKey;
    private String teamsApiUrl = apiUrl + "?action=get_teams&APIkey=" + apiKey;

    public Flux<Country> collectCountriesData() {
        return fetchCountriesData()
                .flatMap(this::saveCountry)
                .doOnComplete(() -> log.info("Countries data collected successfully"));
    }

    public Flux<League> collectLeaguesData() {
        return fetchLeaguesData()
                .flatMap(this::saveLeague)
                .doOnComplete(() -> log.info("Leagues data collected successfully"));
    }

    public Flux<Team> collectTeamsData() {
        return fetchTeamsData()
                .flatMap(this::enrichTeamWithPlayers)
                .flatMap(this::saveTeam)
                .doOnComplete(() -> log.info("Teams data collected successfully"));
    }

    private Flux<Country> fetchCountriesData() {
        return webClientBuilder.build()
                .get()
                .uri(countriesApiUrl)
                .retrieve()
                .bodyToFlux(Country.class)
                .doOnNext(country -> log.info("Country fetched: {}", country))
                .onErrorResume(error -> {
                    log.error("Error occurred while fetching countries data: {}", error.getMessage());
                    return Flux.empty();
                });
    }

    private Flux<Team> fetchTeamsData() {
        return webClientBuilder.build()
                .get()
                .uri(teamsApiUrl)
                .retrieve()
                .bodyToFlux(Team.class)
                .doOnNext(team -> log.info("Team fetched: {}", team.getTeamName()))
                .onErrorResume(error -> {
                    log.error("Error occured while fetching teams data: {}", error.getMessage());
                    return Flux.empty();
                });
    }

    private Mono<Team> saveTeam(Team team) {
        return teamRepository.save(team)
                .doOnSuccess(savedTeam ->
                        log.info("Saved team: {} with {} players",
                                savedTeam.getTeamName(),
                                savedTeam.getPlayers().size()))
                .onErrorResume(error -> {
                    log.error("Error saving team {}: {}", team.getTeamName(), error.getMessage());
                    return Mono.empty();
                });
    }

}
