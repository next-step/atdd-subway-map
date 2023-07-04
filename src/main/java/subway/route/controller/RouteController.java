package subway.route.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> inquiryRoute(@PathVariable Long id) {
        RouteResponse route = routeService.inquiryRoute(id);
        return ResponseEntity.ok().body(route);
    }


}
