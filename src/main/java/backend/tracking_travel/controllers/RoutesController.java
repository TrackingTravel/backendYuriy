package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.Country;
import backend.tracking_travel.entities.FileGPX;
import backend.tracking_travel.entities.Route;
import backend.tracking_travel.gpxWriteRead.GpxReader;
import backend.tracking_travel.gpxWriteRead.Track;
import backend.tracking_travel.repo.FileGpxRepository;
import backend.tracking_travel.services.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static backend.tracking_travel.controllers.FileController.uploadGpx;
import static backend.tracking_travel.controllers.FileController.uploadMultiplePhoto;

@RestController
@RequestMapping("api/routes")
public class RoutesController {
    private final RouteService routeService;
    private final FileGpxRepository fileGpxRepository;

    public RoutesController(RouteService routeService, FileGpxRepository fileGpxRepository) {
        this.routeService = routeService;
        this.fileGpxRepository = fileGpxRepository;
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут и сохранить его в БД")
    public ResponseEntity<?> create(@RequestParam("title") String title, @RequestParam("description") String description,
                                    @RequestParam("gpx") MultipartFile gpx, @RequestParam("photo") MultipartFile[] photo) {
        Route route = new Route();
        route.setTitle(title);
        route.setDescription(description);
        route.setCountry(new Country(1L, "MONTENEGRO"));

        route.setFileGPX(uploadGpx(gpx));

        route.setPhotos(uploadMultiplePhoto(photo));

        routeService.addRoute(route);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/getPoints/{id}")
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

    @GetMapping(value = "/getAll")
    @Operation(summary = "Запрос всех маршрутов", description = "Позволяет запросить все маршруты из БД")
    public ResponseEntity<List<Route>> getAllRoutes() {
        final List<Route> routes = routeService.findAllRoutes();
        return routes != null && !routes.isEmpty()
                ? new ResponseEntity<>(routes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Запрос маршрута по его ID", description = "Позволяет запросить маршрут по его ID из БД")
    public ResponseEntity<Route> getRouteById(@PathVariable(name = "id") Long id) {
        final Optional<Route> optionalRoute = routeService.findRouteById(id);
        if (optionalRoute.isPresent()) {
            final Route route = optionalRoute.get();
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение маршрута по его ID", description = "Позволяет изменить маршрут по его ID из БД")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody Route route) {
        final boolean updated = routeService.updateRoute(route, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление маршрута по его ID", description = "Позволяет удалить маршрут по его ID из БД")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = routeService.deleteRoute(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }


}
