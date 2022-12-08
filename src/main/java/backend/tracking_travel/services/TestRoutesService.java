package backend.tracking_travel.services;

import backend.tracking_travel.entities.TestRoute;
import backend.tracking_travel.repo.TestRoutesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestRoutesService {
    private final TestRoutesRepository testRoutesRepository;

    public TestRoutesService(TestRoutesRepository testRoutesRepository) {
        this.testRoutesRepository = testRoutesRepository;
    }
    public void addRoute (TestRoute route) {
        testRoutesRepository.save(route);
    }

    public List<TestRoute> findAllRoutes() {
        return testRoutesRepository.findAll();
    }

    public Optional<TestRoute> findRouteById (Long id) {
        return testRoutesRepository.findById(id);
    }

    public boolean updateRoute (TestRoute route, Long id) {
        if (testRoutesRepository.existsById(id)){
            route.setId(id);
            testRoutesRepository.save(route);
            return true;
        }
        return false;
    }

    public boolean deleteRoute (Long id) {
        if (testRoutesRepository.existsById(id)) {
            testRoutesRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
