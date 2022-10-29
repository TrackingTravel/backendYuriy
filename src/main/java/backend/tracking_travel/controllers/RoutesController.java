package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.Route;
import backend.tracking_travel.services.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/routes")
public class RoutesController {

    private final RouteService routeService;

    @PostMapping(value = "/create")
    @Operation(summary = "Создание нового маршрута", description = "Позволяет создать новый маршрут")
    public ResponseEntity<?> create (@RequestBody Route route) {
        routeService.addRoute(route);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
