package hu.szarvas.data_collector.repository;

import hu.szarvas.data_collector.model.Team;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamRepository extends ReactiveMongoRepository<Team, String> {
    Mono<Team> findByTeamKey(String teamKey);
    Flux<Team> findByLeagueId(String leagueId);
}