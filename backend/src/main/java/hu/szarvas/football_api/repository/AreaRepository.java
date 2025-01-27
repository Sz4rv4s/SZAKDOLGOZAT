package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.Area;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends MongoRepository<Area, Integer> {
}
