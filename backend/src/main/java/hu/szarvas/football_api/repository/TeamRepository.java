package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}
