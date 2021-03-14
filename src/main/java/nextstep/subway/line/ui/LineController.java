package nextstep.subway.line.ui;

import nextstep.subway.common.exception.InvalidRequestException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lineResponseList = lineService.getLineList();
        return ResponseEntity.ok(lineResponseList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lineService.getLine(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateLine(@PathVariable("id") Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable("id") Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity handleIllegalArgsException(InvalidRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
