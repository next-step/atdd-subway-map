package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable(name = "id") long lineId) {
        LineResponse lineById = lineService.findLineById(lineId);
        return ResponseEntity.ok().body(lineById);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable(name = "id") long lineId, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(lineId, lineRequest);
        return ResponseEntity.ok().build();
    }
}
