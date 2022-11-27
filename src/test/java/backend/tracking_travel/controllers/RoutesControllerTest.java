package backend.tracking_travel.controllers;

import backend.tracking_travel.entities.Country;
import backend.tracking_travel.entities.Route;
import backend.tracking_travel.repo.FileGpxRepository;
import backend.tracking_travel.repo.RoutesRepository;
import backend.tracking_travel.services.RouteService;
import backend.tracking_travel.services.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoutesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RoutesRepository routesRepository;
    @MockBean
    RouteService routeService;
    @MockBean
    StorageService storageService;
    @MockBean
    FileGpxRepository fileGpxRepository;

    Route ROUTE_1 = new Route(1L, "Example_1", "Example_description_1", (new Country(1L, "MONTENEGRO")));
    Route ROUTE_2 = new Route(2L, "Example_2", "Example_description_2", (new Country(1L, "MONTENEGRO")));

    @Test
    void create() {
    }

    @Test
    void getPointsOfRouteById() {
    }

    @Test
    void getAllRoutes() throws Exception {
        List<Route> routes = new ArrayList<>(Arrays.asList(ROUTE_1, ROUTE_2));
        // когда происходит вызов метода в него подставляется List routes
        Mockito.when(routeService.findAllRoutes()).thenReturn(routes);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/routes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].title", is("Example_2")));
    }

    @Test
    void getRouteById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}