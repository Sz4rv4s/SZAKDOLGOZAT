package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
}
