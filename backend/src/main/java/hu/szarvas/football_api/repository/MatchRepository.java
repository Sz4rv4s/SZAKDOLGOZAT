package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Match;
import hu.szarvas.football_api.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Repository
public interface MatchRepository extends MongoRepository<Match, Integer> {
    Match getMatchById(Integer matchId);

    List<Match> getMatchByCompetitionIdAndStatusInAndUtcDateBetween(Integer competitionId, Collection<Status> statuses, Instant utcDateAfter, Instant utcDateBefore);
}
