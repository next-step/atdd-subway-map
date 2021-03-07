package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.DuplicateLineException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> getAllLines() {
        List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> getLine(@PathVariable("lineId") Long lineId) {
        LineResponse line = lineService.findLine(lineId);
        return ResponseEntity.ok().body(line);
    }

    @PatchMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLine(@PathVariable("lineId") Long lineId, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(lineId, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteLine(@PathVariable("lineId") Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(DuplicateLineException.class)
    public ResponseEntity handleDuplicateLineException(DuplicateLineException e) {
        HashMap<String, String> resultBody = new HashMap();
        resultBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(resultBody);
    }
}
