package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exceptions.ExistingLineException;
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

    @GetMapping
    public ResponseEntity findAllLines() {
        List<LineResponse> allLine = lineService.findAllLines();
        return ResponseEntity.ok().body(allLine);
    }

    @GetMapping("/{id}")
    public ResponseEntity findLine(@PathVariable long id) {
        try {
            LineResponse line = lineService.findLine(id);
            return ResponseEntity.ok().body(line);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("존재하지 않는 Line에 대한 요청입니다.");
        }
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        try {
            LineResponse line = lineService.saveLine(lineRequest);
            return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
        } catch (ExistingLineException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(
        @RequestBody LineRequest lineRequest,
        @PathVariable long id
    ) {
        LineResponse line = lineService.updateLine(lineRequest, id);
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeLine(@PathVariable long id) {
        try {
            lineService.removeLine(id);
            return ResponseEntity.noContent().build();
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("존재하지 않는 Line에 대한 요청입니다.");
        }
    }
}
