package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends MongoRepository<Match, Integer> {
    Match getMatchById(Integer matchId);
}
