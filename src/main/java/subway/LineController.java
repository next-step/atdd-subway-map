package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.AddLineRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse line = lineService.createLine(request);
        return ResponseEntity.created(URI.create("/line/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findLines() {
        return ResponseEntity.ok(lineService.findLines());
    }

    @PostMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLine(id));
    }

    @PutMapping("/{id}")
    public void updateLine(@PathVariable Long id, @RequestBody LineRequest request) {
        lineService.updateLine(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<LineResponse> addLine(@PathVariable Long id, @RequestBody AddLineRequest dto) {
        LineResponse line = lineService.addLineStation(id, dto);
        return ResponseEntity.created(URI.create("/line/" + line.getId() + "/stations")).body(line);
    }

    @DeleteMapping("/{id}/sections")
    public void deleteLineDownStation(@PathVariable Long id, @RequestParam("stationId")Long stationId) {
        lineService.deleteLineDownStation(id, stationId);
    }
}
