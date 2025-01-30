package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Competition;
import hu.szarvas.football_api.model.CompetitionType;
import hu.szarvas.football_api.model.TierPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends MongoRepository<Competition, Integer> {
    List<Competition> findByPlanAndType(TierPlan plan, CompetitionType type);

    List<Competition> findByType(CompetitionType type);

    Competition getCompetitionById(Integer id);
}
