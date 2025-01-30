package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.UserPoints;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPointsRepository extends MongoRepository<UserPoints, Integer> {
    List<UserPoints> findByMatchDateBetween(Instant start, Instant end);
    List<UserPoints> findByUserId(Integer userId);

    Optional<UserPoints> getUserPointsByUserIdAndMatchId(Integer userId, Integer matchId);
}
