package nextstep.subway.line.ui;

import nextstep.subway.line.NotFoundLineException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }


    @GetMapping("/lines")
    public ResponseEntity findAllLines(){
        List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @PostMapping("/lines")
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity findLineById(@PathVariable Long id) {
        LineResponse line = lineService.findLineById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity notFoundLineExceptionHandler(NotFoundLineException e) {
        String errorMessage = e.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
