package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
}
