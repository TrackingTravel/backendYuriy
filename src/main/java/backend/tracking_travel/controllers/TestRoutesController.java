package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.*;
import backend.tracking_travel.gpxWriteRead.GpxReader;
import backend.tracking_travel.gpxWriteRead.Track;
import backend.tracking_travel.repo.FileGpxRepository;
import backend.tracking_travel.services.StorageService;
import backend.tracking_travel.services.TestRoutesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/test-routes")
public class TestRoutesController {
    private final TestRoutesService testRoutesService;
    private final StorageService storageService;

    public TestRoutesController(TestRoutesService testRoutesService, StorageService storageService) {
        this.testRoutesService = testRoutesService;
        this.storageService = storageService;
    }


    @PostMapping(value = "/create")
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут и сохранить его в БД")
    public ResponseEntity<?> create(@RequestParam("title") String title, @RequestParam("description") String description,
                                    @RequestParam("mapLink") String linkToMap, @RequestParam("mapPhoto") MultipartFile mapPhoto,
                                    @RequestParam("photo") MultipartFile[] photo, @RequestParam("peak") Long peak,
                                    @RequestParam("distance") Long distance, @RequestParam("duration") Long duration) {
        TestRoute route = new TestRoute();
        route.setTitle(title);
        route.setDescription(description);
        route.setCountry(new Country(1L, "MONTENEGRO"));
        route.setLinkToMap(linkToMap);
        route.setMapPhoto((MapPhoto) storageService.storePhoto(mapPhoto));
        route.setPhoto(storageService.multiStorePhoto(photo));
        route.setHeightPeak(peak);
        route.setDistanceRoute(distance);
        route.setDurationRoute(duration);

        testRoutesService.addRoute(route);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/getAll")
    @Operation(summary = "Запрос всех маршрутов", description = "Позволяет запросить все маршруты из БД")
    public ResponseEntity<List<TestRoute>> getAllRoutes() {
        final List<TestRoute> routes = testRoutesService.findAllRoutes();
        return routes != null && !routes.isEmpty()
                ? new ResponseEntity<>(routes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Запрос маршрута по его ID", description = "Позволяет запросить маршрут по его ID из БД")
    public ResponseEntity<TestRoute> getRouteById(@PathVariable(name = "id") Long id) {
        final Optional<TestRoute> optionalRoute = testRoutesService.findRouteById(id);
        if (optionalRoute.isPresent()) {
            final TestRoute route = optionalRoute.get();
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение маршрута по его ID", description = "Позволяет изменить маршрут по его ID из БД")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody TestRoute route) {
        final boolean updated = testRoutesService.updateRoute(route, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление маршрута по его ID", description = "Позволяет удалить маршрут по его ID из БД")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = testRoutesService.deleteRoute(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
