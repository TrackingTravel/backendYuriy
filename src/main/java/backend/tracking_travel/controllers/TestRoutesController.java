package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.*;
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
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут и сохранить его в БД. " +
            "Параметры запроса: title - название маршрута (формат string); description - описание маршрута (формат string);" +
            "mapLink - ссылка на карту (формат string); mapPhoto - скриншот карты с проложенным маршрутом (формат file);" +
            "photo - фотографии пейзажей маршрута (формат массив файлов file); peak - пиковая высота над уровнем моря на маршруте (формат string);" +
            "distance - дистанция маршрута (формат string); duration - продолжительность маршрута (формат string)")
    public ResponseEntity<?> create(@RequestParam("title") String title, @RequestParam("description") String description,
                                    @RequestParam("mapLink") String linkToMap, @RequestParam("mapPhoto") MultipartFile mapPhoto,
                                    @RequestParam("photo") MultipartFile[] photo, @RequestParam("peak") String peak,
                                    @RequestParam("distance") String distance, @RequestParam("duration") String duration) {
        TestRoute route = new TestRoute();
        route.setTitle(title);
        route.setDescription(description);
        route.setCountry(new Country(1L, "MONTENEGRO"));
        route.setLinkToMap(linkToMap);
        route.setMapPhoto(storageService.storeMapPhoto(mapPhoto));
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
