package subway.route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest routeRequest) {
        LineResponse lineResponse = lineService.saveLine(routeRequest);
        return ResponseEntity.created(URI.create("/lines/1")).body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.findAllStations();
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(
            @PathVariable(name = "id") Long id) {
        LineResponse lineResponse = new LineResponse(id, "신분당선", "bg-red-600", null);
        return ResponseEntity.ok(lineResponse);
    }
}
