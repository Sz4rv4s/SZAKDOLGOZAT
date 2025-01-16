package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompetitionRepository extends MongoRepository<Competition, String> {
    List<Competition> findByPlanAndType(String plan, String type);
}
