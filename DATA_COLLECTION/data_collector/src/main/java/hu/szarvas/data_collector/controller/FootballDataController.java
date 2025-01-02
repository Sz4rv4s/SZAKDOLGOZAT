package hu.szarvas.data_collector.controller;

import hu.szarvas.data_collector.model.Country;
import hu.szarvas.data_collector.model.League;
import hu.szarvas.data_collector.model.Team;
import hu.szarvas.data_collector.repository.CountryRepository;
import hu.szarvas.data_collector.repository.LeagueRepository;
import hu.szarvas.data_collector.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class FootballDataController {

    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public FootballDataController(
            CountryRepository countryRepository,
            LeagueRepository leagueRepository,
            TeamRepository teamRepository
    ) {
        this.countryRepository = countryRepository;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping(path = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @GetMapping(path = "/countries/{countryName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Country> getCountryByName(@PathVariable String countryName) {
        return countryRepository.findByCountryName(countryName);
    }

    @GetMapping(path = "/leagues", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    @GetMapping(path = "/countries/{countryId}/leagues", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<League> getLeaguesByCountry(@PathVariable ObjectId countryId) {
        return leagueRepository.findByCountryId(countryId);
    }

    @GetMapping(path = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping(path = "/teams/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Team> getTeamByTeamKey(@PathVariable String key) {
        return teamRepository.findByTeamKey(key);
    }

    @GetMapping(path = "/leagues/{leagueId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Team> getTeamsByLeague(@PathVariable String leagueId) {
        return teamRepository.findAll();
    }
}
