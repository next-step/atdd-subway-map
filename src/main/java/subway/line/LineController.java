package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.CreateSectionRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.UpdateLineRequest;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
        LineResponse response = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody UpdateLineRequest request) {
        lineService.updateLineById(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody CreateSectionRequest request) {
        LineResponse response = lineService.addSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
}
