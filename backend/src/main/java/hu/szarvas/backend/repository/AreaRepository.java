package hu.szarvas.backend.repository;

import hu.szarvas.backend.model.Area;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AreaRepository extends MongoRepository<Area, Integer> {
}
