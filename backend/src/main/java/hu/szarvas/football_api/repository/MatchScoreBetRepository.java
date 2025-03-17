package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.BetStatus;
import hu.szarvas.football_api.model.MatchScoreBet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchScoreBetRepository extends MongoRepository<MatchScoreBet, Integer> {
    boolean existsByUserIdAndMatchId(Integer userId, Integer matchId);
    List<MatchScoreBet> findByStatus(BetStatus status);
    Optional<MatchScoreBet> findByUserIdAndMatchId(Integer userId, Integer matchId);

    List<MatchScoreBet> getMatchScoreBetByUserId(Integer userId);
}
