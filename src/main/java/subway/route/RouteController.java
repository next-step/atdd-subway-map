package subway.route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/routes")
    public ResponseEntity<RouteResponse> createStation(@RequestBody RouteRequest routeRequest) {
        RouteResponse routeResponse = routeService.saveRoute(routeRequest);
        return ResponseEntity.created(URI.create("/routes/1")).body(routeResponse);
    }

}
