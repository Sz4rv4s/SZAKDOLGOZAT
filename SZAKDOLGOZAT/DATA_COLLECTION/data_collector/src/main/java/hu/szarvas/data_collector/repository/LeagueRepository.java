package hu.szarvas.data_collector.repository;

import hu.szarvas.data_collector.model.League;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface LeagueRepository extends ReactiveMongoRepository<League, String> {
    Flux<League> findByCountryId(ObjectId countryId);
}
