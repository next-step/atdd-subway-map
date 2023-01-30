package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> readLines() {
        List<LineResponse> line = lineService.readLines();
        return ResponseEntity.ok().body(line);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> readLine(@PathVariable Long id) {
        LineResponse line = lineService.readLine(id);
        return ResponseEntity.ok(line);
    }
}
