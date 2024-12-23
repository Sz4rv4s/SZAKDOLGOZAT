package hu.szarvas.data_collector.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import hu.szarvas.data_collector.model.Country;
import reactor.core.publisher.Mono;

public interface CountryRepository extends ReactiveMongoRepository<Country, String> {
    Mono<Country> findByCountryName(String countryName);
}
