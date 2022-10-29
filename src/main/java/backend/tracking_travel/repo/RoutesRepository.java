package backend.tracking_travel.repo;

import backend.tracking_travel.entities.Route;
import org.springframework.data.repository.CrudRepository;

public interface RoutesRepository extends CrudRepository<Route, Long> {

}
