package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.*;
import backend.tracking_travel.services.StorageService;
import backend.tracking_travel.services.TestRoutesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
public class TestRoutesController {
    private static final Logger logger = LoggerFactory.getLogger(TestRoutesController.class);

    private final TestRoutesService testRoutesService;
    private final StorageService storageService;

    @Autowired
    public TestRoutesController(TestRoutesService testRoutesService, StorageService storageService) {
        this.testRoutesService = testRoutesService;
        this.storageService = storageService;
    }

    @PostMapping(value = "/test-route/create")
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут и сохранить его в БД")
    public ResponseEntity<?> create(@RequestParam("title") @Parameter(description = "Название маршрута") String title,
                                    @RequestParam("description") @Parameter(description = "Описание маршрута") String description,
                                    @RequestParam("mapLink") @Parameter(description = "Ссылка на маршрут") String linkToMap,
                                    @RequestParam("mapPhoto") @Parameter(description = "Массив скриншотов карты") MultipartFile[] mapPhoto,
                                    @RequestParam("photo") @Parameter(description = "Массив фотографий маршрута") MultipartFile[] photo,
                                    @RequestParam("peak") @Parameter(description = "Пиковая высота на маршруте") String peak,
                                    @RequestParam("distance") @Parameter(description = "Протяжённость маршрута") String distance,
                                    @RequestParam("duration") @Parameter(description = "Продолжительность маршрута") String duration) {
        TestRoute route = new TestRoute();
        route.setTitle(title);
        route.setDescription(description);
        route.setCountry(new Country(1L, "MONTENEGRO"));
        route.setLinkToMap(linkToMap);
        route.setMapPhoto(storageService.multiStoreMapPhoto(mapPhoto));
        route.setPhoto(storageService.multiStorePhoto(photo));
        route.setHeightPeak(peak);
        route.setDistanceRoute(distance);
        route.setDurationRoute(duration);

        testRoutesService.addRoute(route);
        logger.info("Маршрут " + title + " успешно создан");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/test-routes")
    @Operation(summary = "Запрос всех маршрутов", description = "Позволяет запросить все маршруты из БД")
    public ResponseEntity<List<TestRoute>> getAllRoutes() {
        final List<TestRoute> routes = testRoutesService.findAllRoutes();

        logger.info("Маршруты успешно запрошены");
        return routes != null && !routes.isEmpty()
                ? new ResponseEntity<>(routes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/test-route/{id}")
    @Operation(summary = "Запрос маршрута по его ID", description = "Позволяет запросить маршрут по его ID из БД")
    public ResponseEntity<TestRoute> getRouteById(@PathVariable(name = "id") Long id) {
        final Optional<TestRoute> optionalRoute = testRoutesService.findRouteById(id);
        if (optionalRoute.isPresent()) {
            final TestRoute route = optionalRoute.get();

            logger.info("Показан маршрут с ID:" + id);
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        logger.info("Маршрут с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/test-route/{id}")
    @Operation(summary = "Изменение маршрута по его ID", description = "Позволяет изменить маршрут по его ID из БД")
    public ResponseEntity<?> update(@PathVariable(name = "id") @Parameter(description = "ID маршрута") Long id,
                                    @RequestParam("title") @Parameter(description = "Название маршрута") String title,
                                    @RequestParam("description") @Parameter(description = "Описание маршрута") String description,
                                    @RequestParam("mapLink") @Parameter(description = "Ссылка на маршрут") String linkToMap,
                                    @RequestParam("mapPhoto") @Parameter(description = "Массив скриншотов карты") MultipartFile[] mapPhoto,
                                    @RequestParam("photo") @Parameter(description = "Массив фотографий маршрута") MultipartFile[] photo,
                                    @RequestParam("peak") @Parameter(description = "Пиковая высота на маршруте") String peak,
                                    @RequestParam("distance") @Parameter(description = "Протяжённость маршрута") String distance,
                                    @RequestParam("duration") @Parameter(description = "Продолжительность маршрута") String duration) {
        TestRoute updateRoute = new TestRoute();
        updateRoute.setTitle(title);
        updateRoute.setDescription(description);
        updateRoute.setCountry(new Country(1L, "MONTENEGRO"));
        updateRoute.setLinkToMap(linkToMap);
        updateRoute.setMapPhoto(storageService.multiStoreMapPhoto(mapPhoto));
        updateRoute.setPhoto(storageService.multiStorePhoto(photo));
        updateRoute.setHeightPeak(peak);
        updateRoute.setDistanceRoute(distance);
        updateRoute.setDurationRoute(duration);

        final boolean updated = testRoutesService.updateRoute(updateRoute, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/test-route/{id}")
    @Operation(summary = "Удаление маршрута по его ID", description = "Позволяет удалить маршрут по его ID из БД")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = testRoutesService.deleteRoute(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
