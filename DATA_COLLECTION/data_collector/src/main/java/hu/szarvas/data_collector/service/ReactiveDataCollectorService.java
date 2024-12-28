package hu.szarvas.data_collector.service;

import hu.szarvas.data_collector.model.Country;
import hu.szarvas.data_collector.model.League;
import hu.szarvas.data_collector.model.Team;
import hu.szarvas.data_collector.repository.CountryRepository;
import hu.szarvas.data_collector.repository.LeagueRepository;
import hu.szarvas.data_collector.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveDataCollectorService {

    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final WebClient webClient;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    private static final Map<String, String> TOP_LEAGUES = Map.of(
            "England", "Premier League",
            "Germany", "Bundesliga",
            "Spain", "La Liga",
            "France", "Ligue 1",
            "Italy", "Serie A",
            "Hungary", "NB I"
    );

    private static final Set<String> TARGET_COUNTRIES = TOP_LEAGUES.keySet();

    public Flux<Country> fetchAndSaveAllCountries() {
        String url = apiUrl + "?action=get_countries&APIkey=" + apiKey;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Country.class)
                .map(country -> {
                    country.setId(new ObjectId());
                    return country;
                })
                .flatMap(countryRepository::save)
                .doOnNext(country -> log.debug("Saved country: {}", country.getCountryName()))
                .doOnError(error -> log.error("Error fetching/saving countries: {}", error.getMessage()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
    }

    private Flux<Country> getTargetCountriesFromDb() {
        return countryRepository.findAll()
                .filter(country -> TARGET_COUNTRIES.contains(country.getCountryName()))
                .doOnNext(country -> log.debug("Found target country: {} with ID: {}",
                        country.getCountryName(), country.getId()));
    }

    public Flux<League> fetchAndSaveTopLeagues() {
        return getTargetCountriesFromDb()
                .flatMap(country -> {
                    String url = String.format("%s?action=get_leagues&country_id=%s&APIkey=%s",
                            apiUrl, country.getId(), apiKey);

                    return webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToFlux(League.class)
                            .filter(league -> {
                                String topLeagueName = TOP_LEAGUES.get(country.getCountryName());
                                return league.getLeagueName().contains(topLeagueName);
                            })
                            .map(league -> {
                                league.setLeagueId(new ObjectId());
                                league.setCountryId(country.getId());
                                league.setCountryName(country.getCountryName());
                                league.setCountryLogo(country.getCountryLogo());
                                return league;
                            })
                            .flatMap(leagueRepository::save)
                            .doOnNext(league -> log.debug("Saved league: {} for country: {}",
                                    league.getLeagueName(), league.getCountryName()));
                })
                .doOnError(error -> log.error("Error fetching/saving leagues: {}", error.getMessage()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
    }

    public Flux<Team> fetchAndSaveTeamsForTopLeagues() {
        return leagueRepository.findAll()
                .flatMap(league -> {
                    String url = String.format("%s?action=get_teams&league_id=%s&APIkey=%s",
                            apiUrl, league.getLeagueId(), apiKey);

                    return webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToFlux(Team.class)
                            .map(team -> {
                                team.setLeagueId(league.getLeagueId().toString());
                                return team;
                            })
                            .flatMap(team -> teamRepository.findByTeamKey(team.getTeamKey())
                                    .flatMap(existingTeam -> {
                                        team.setId(existingTeam.getId());
                                        return teamRepository.save(team);
                                    })
                                    .switchIfEmpty(teamRepository.save(team)));
                })
                .doOnNext(team -> log.debug("Saved team: {}", team.getTeamName()))
                .doOnError(error -> log.error("Error fetching/saving teams: {}", error.getMessage()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
    }

    public Mono<Void> collectAllData() {
        return fetchAndSaveAllCountries()
                .thenMany(fetchAndSaveTopLeagues())
                .thenMany(fetchAndSaveTeamsForTopLeagues())
                .then()
                .doOnSuccess(_ -> log.info("Completed full data collection"))
                .doOnError(e -> log.error("Error during full data collection", e));
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void scheduleDataCollection() {
        log.info("Starting scheduled data collection at {}", LocalDateTime.now());
        collectAllData()
                .doOnSuccess(_ -> log.info("Completed scheduled data collection at {}", LocalDateTime.now()))
                .subscribe();
    }
}