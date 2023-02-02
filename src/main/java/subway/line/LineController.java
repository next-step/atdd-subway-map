package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse lineResponse = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest request) {
        lineService.updateLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
