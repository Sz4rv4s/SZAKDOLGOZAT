package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}
