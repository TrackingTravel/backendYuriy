package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.Country;
import backend.tracking_travel.entities.FileGPX;
import backend.tracking_travel.entities.Route;
import backend.tracking_travel.gpxWriteRead.GpxReader;
import backend.tracking_travel.gpxWriteRead.Track;
import backend.tracking_travel.repo.FileGpxRepository;
import backend.tracking_travel.services.RouteService;
import backend.tracking_travel.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class RoutesController {
    private final RouteService routeService;
    private final StorageService storageService;
    private final FileGpxRepository fileGpxRepository;

    @Autowired
    public RoutesController(RouteService routeService, StorageService storageService, FileGpxRepository fileGpxRepository) {
        this.routeService = routeService;
        this.storageService = storageService;
        this.fileGpxRepository = fileGpxRepository;
    }

    @PostMapping(value = "route/create")
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут и сохранить его в БД")
    public ResponseEntity<?> create(@RequestParam("title") @Valid String title, @RequestParam("description") @Valid String description,
                                    @RequestParam("gpx") MultipartFile gpx, @RequestParam("photo") MultipartFile[] photo) {
        Route route = new Route();
        route.setTitle(title);
        route.setDescription(description);
        route.setCountry(new Country(1L, "MONTENEGRO"));

        route.setFileGPX(storageService.storeGPX(gpx));

        route.setPhotos(storageService.multiStorePhoto(photo));

        routeService.addRoute(route);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "routePoints/{id}")
    @Operation(summary = "Запрос данных трэка по ID файла gpx", description = "Позволяет запросить данные трэка по ID файла gpx из БД")
    public ResponseEntity<Track> getPointsOfRouteById(@PathVariable(name = "id") Long id) {
        Optional<FileGPX> fileGPX = fileGpxRepository.findById(id);
        if (fileGPX.isPresent()) {
            GpxReader gpxReader = new GpxReader("uploads/" + fileGPX.get().getName());
            Track track = gpxReader.readData();

            return new ResponseEntity<>(track, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/routes")
    @Operation(summary = "Запрос всех маршрутов", description = "Позволяет запросить все маршруты из БД")
    public ResponseEntity<List<Route>> getAllRoutes() {
        final List<Route> routes = routeService.findAllRoutes();
        return routes != null && !routes.isEmpty()
                ? new ResponseEntity<>(routes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "route/{id}")
    @Operation(summary = "Запрос маршрута по его ID", description = "Позволяет запросить маршрут по его ID из БД")
    public ResponseEntity<Route> getRouteById(@PathVariable(name = "id") Long id) {
        final Optional<Route> optionalRoute = routeService.findRouteById(id);
        if (optionalRoute.isPresent()) {
            final Route route = optionalRoute.get();
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "route/{id}")
    @Operation(summary = "Изменение маршрута по его ID", description = "Позволяет изменить маршрут по его ID из БД")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody Route route) {
        final boolean updated = routeService.updateRoute(route, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "route/{id}")
    @Operation(summary = "Удаление маршрута по его ID", description = "Позволяет удалить маршрут по его ID из БД")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = routeService.deleteRoute(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }


}
