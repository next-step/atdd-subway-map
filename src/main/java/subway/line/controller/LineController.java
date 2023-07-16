package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.code.LineValidateTypeCode;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService routeService;

    public LineController(LineService routeService) {
        this.routeService = routeService;
    }

    @PostMapping()
    public ResponseEntity<LineResponse> createRoute(@RequestBody LineRequest routeRequest) {
        LineResponse route = routeService.saveRoute(LineValidateTypeCode.SAVE, routeRequest);
        return ResponseEntity.created(URI.create("/api/routes/" + route.getId())).body(route);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> inquiryRoute(@PathVariable Long id) {
        LineResponse route = routeService.inquiryRoute(id);
        return ResponseEntity.ok().body(route);
    }

    @GetMapping("")
    public ResponseEntity<List<LineResponse>> inquiryRoutes() {
        List<LineResponse> routes = routeService.inquiryRoutes();
        return ResponseEntity.ok().body(routes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> updateRoute(@PathVariable Long id, @RequestBody LineRequest routeRequest) {
        routeRequest.saveId(id);
        LineResponse route = routeService.saveRoute(LineValidateTypeCode.UPDATE, routeRequest);
        return ResponseEntity.ok().body(route);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<LineResponse> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }


}
