package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, Integer> {
}
