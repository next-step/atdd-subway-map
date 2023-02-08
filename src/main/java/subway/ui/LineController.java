package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

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
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable(name = "id") long lineId) {
        LineResponse lineResponse = lineService.findLineById(lineId);

        return ResponseEntity.ok().body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.findAllLines();

        return ResponseEntity.ok().body(lineResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable(name = "id") long lineId, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(lineId, lineRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable(name = "id") long lineId) {
        lineService.deleteLineById(lineId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable(name = "id") Long lineId, @RequestBody SectionRequest sectionRequest) {
        LineResponse lineResponse = lineService.addSection(lineId, sectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId() + "/sections")).body(lineResponse);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable(name = "id") Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class})
    public ResponseEntity<Void> catchNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Void> catchStandardException() {
        return ResponseEntity.badRequest().build();
    }
}
