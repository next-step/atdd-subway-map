package subway.line;

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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/line/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findById(id));
    }

    @PutMapping("/line/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest) {
        return ResponseEntity.ok().body(lineService.updateLine(id, lineUpdateRequest));
    }

    @DeleteMapping("/line/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/selections")
    public ResponseEntity<LineAppendResponse> appendLine(@PathVariable Long id, @RequestBody LineAppendRequest lineAppendRequest) {
        return ResponseEntity.ok().body(lineService.appendLine(id, lineAppendRequest));
    }

    @DeleteMapping("/lines/{id}/selections")
    public ResponseEntity<LineRemoveResponse> removeLine(@PathVariable Long id, @RequestParam Long stationId) {
        return ResponseEntity.ok().body(lineService.removeLine(id, stationId));
    }
}
