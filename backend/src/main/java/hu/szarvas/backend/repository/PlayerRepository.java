package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, Integer> {
}
