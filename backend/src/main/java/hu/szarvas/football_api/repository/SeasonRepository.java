package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Season;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeasonRepository extends MongoRepository<Season, Integer> {
}
