package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.Route;
import backend.tracking_travel.services.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/routes")
public class RoutesController {
    private final RouteService routeService;

    public RoutesController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут и сохранить его в БД")
    public ResponseEntity<?> create(@RequestBody Route route) {
        routeService.addRoute(route);
        System.out.println(route.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
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
    public ResponseEntity<?> update (@PathVariable (name = "id") Long id, @RequestBody Route route){
        final boolean updated = routeService.updateRoute(route, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление маршрута по его ID", description = "Позволяет удалить маршрут по его ID из БД")
    public ResponseEntity<?> delete (@PathVariable (name = "id") Long id) {
        final boolean deleted = routeService.deleteRoute(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }


}
