package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exceptions.ExistingLineException;
import nextstep.subway.line.exceptions.NotFoundLineException;
import nextstep.subway.line.exceptions.NotFoundSectionException;
import nextstep.subway.line.exceptions.IsNotValidUpStationException;
import nextstep.subway.line.exceptions.IsDownStationExistedException;
import nextstep.subway.station.exception.NotFoundStationException;
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
        LineResponse line = lineService.findLine(id);
        return ResponseEntity.ok().body(line);
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
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
        lineService.removeLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity addLineStation(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        lineService.addLineStation(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity removeLineStation(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.removeLineStation(id, stationId);
        return ResponseEntity.noContent().build();
    }





    @ExceptionHandler(ExistingLineException.class)
    public ResponseEntity handleExistingLineException(ExistingLineException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity handleNotFoundLineException(NotFoundLineException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundStationException.class)
    public ResponseEntity handleNotFoundStationException(NotFoundStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundSectionException.class)
    public ResponseEntity handleNotFoundSectionException(NotFoundSectionException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IsNotValidUpStationException.class)
    public ResponseEntity handleNotLastSectionException(IsNotValidUpStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IsDownStationExistedException.class)
    public ResponseEntity handleOnlyOneSectionException(IsDownStationExistedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
