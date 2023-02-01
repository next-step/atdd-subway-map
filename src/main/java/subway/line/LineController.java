package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
//        LineResponse line = lineService.saveLine(lineRequest);
//        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
        return null;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
//        return ResponseEntity.ok(lineService.findLineResponses());
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity setLine(
            @PathVariable Long id,
            @RequestBody LineRequest lineRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        return null;
    }
}
