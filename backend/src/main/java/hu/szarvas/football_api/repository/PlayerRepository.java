package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, Integer> {
}
