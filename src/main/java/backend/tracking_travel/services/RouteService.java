package backend.tracking_travel.services;

import backend.tracking_travel.entities.Route;
import backend.tracking_travel.repo.RoutesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RouteService {

    private final RoutesRepository routesRepository;

    public void addRoute (Route route) {
        routesRepository.save(route);
    }

    public Iterable<Route> findAllRoutes() {
        return routesRepository.findAll();
    }

    public Optional<Route> findRouteById (Long id) {
        return routesRepository.findById(id);
    }

    public boolean updateRoute (Route route, Long id) {
        if (routesRepository.existsById(id)){
            route.setId(id);
            routesRepository.save(route);
            return true;
        }
        return false;
    }

    public boolean deleteRoute (Long id) {
        if (routesRepository.existsById(id)) {
            routesRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
