package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Season;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeasonRepository extends MongoRepository<Season, Integer> {
}
