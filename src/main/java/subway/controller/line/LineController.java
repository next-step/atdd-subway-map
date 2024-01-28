package subway.controller.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.service.line.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse line = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lines = lineService.listAll();
        return ResponseEntity.ok().body(lines);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> retrieveLine(@PathVariable Long id) {
        LineResponse line = lineService.retrieveBy(id);
        return ResponseEntity.ok().body(line);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LinePatchRequest request) {
        lineService.updateBy(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
        lineService.deleteBy(id);
        return ResponseEntity.noContent().build();
    }
}
