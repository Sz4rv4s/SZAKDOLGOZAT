package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Season;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends MongoRepository<Season, Integer> {
}
