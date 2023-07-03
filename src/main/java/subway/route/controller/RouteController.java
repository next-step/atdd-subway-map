package subway.route.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.route.dto.RouteRequest;
import subway.route.dto.RouteResponse;
import subway.route.service.RouteService;

import java.net.URI;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping()
    public ResponseEntity<RouteResponse> createRoute(@RequestBody RouteRequest routeRequest) {
        RouteResponse route = routeService.saveRoute(routeRequest);
        return ResponseEntity.created(URI.create("/api/routes/" + route.getId())).body(route);
    }


}
