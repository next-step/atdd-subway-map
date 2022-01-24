package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<LineResponse> editLine(
            @PathVariable("id") Long id,
            @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok().body(lineService.updateLine(id, lineRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<LineResponse> deleteLine(@PathVariable("id") Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
