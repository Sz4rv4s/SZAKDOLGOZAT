package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Competition;
import hu.szarvas.backend.model.CompetitionType;
import hu.szarvas.backend.model.TierPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompetitionRepository extends MongoRepository<Competition, String> {
    List<Competition> findByPlanAndType(TierPlan plan, CompetitionType type);
}
