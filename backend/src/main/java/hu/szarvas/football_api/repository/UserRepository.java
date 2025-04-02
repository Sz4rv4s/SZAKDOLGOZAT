package hu.szarvas.football_api.repository;

import hu.szarvas.football_api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndEmail(String email, String username);
    Optional<User> findByRefreshToken(String refreshToken);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User getUserById(Integer id);
}
