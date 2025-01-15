package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompetitionRepository extends MongoRepository<Competition, String> {
}
