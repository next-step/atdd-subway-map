package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.StationService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    protected LineController() {

    }

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) throws Exception {
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping(value="/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }




}
