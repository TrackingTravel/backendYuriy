package backend.tracking_travel.repo;

import backend.tracking_travel.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
