package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Competition;
import hu.szarvas.football_api.model.CompetitionType;
import hu.szarvas.football_api.model.TierPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompetitionRepository extends MongoRepository<Competition, String> {
    List<Competition> findByPlanAndType(TierPlan plan, CompetitionType type);
}
