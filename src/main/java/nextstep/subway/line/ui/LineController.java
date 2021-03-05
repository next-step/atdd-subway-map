package nextstep.subway.line.ui;

import nextstep.subway.error.NameExistsException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<?> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> viewLine(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(lineService.getLine(lineId));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLine());
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<?> updateLine(@PathVariable Long lineId, LineRequest request) {
        lineService.updateLineById(request, lineId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<?> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NameExistsException.class)
    public ResponseEntity handleIllegalArgsException(NameExistsException e) {
        return ResponseEntity.badRequest().build();
    }
}
