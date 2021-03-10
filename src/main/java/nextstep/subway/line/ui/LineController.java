package nextstep.subway.line.ui;

import nextstep.subway.line.application.SectionValidationException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.dto.SectionRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> getAllLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> getLineById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @PutMapping(value = "/lines/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity modifyLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
            lineService.updateLine(id, lineRequest);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
       lineService.deleteLine(id);
       return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/lines/{id}/sections")
    public ResponseEntity addSectionToLine(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        lineService.addSectionToLine(id, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{id}/sections")
    public ResponseEntity deleteStationFromLine(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteStationFromLine(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoSuchElementException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(SectionValidationException.class)
    public ResponseEntity handleSectionValidationException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleSQLIntegrityException() {
        return ResponseEntity.badRequest().build();
    }

}
