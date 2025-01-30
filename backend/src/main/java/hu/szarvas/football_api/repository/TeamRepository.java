package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends MongoRepository<Team, Integer> {
    Team getTeamById(Integer id);
}
