package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.*;
import org.springframework.http.MediaType;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok().body(lineService.getLines());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.getLine(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok().body(lineService.updateLine(id, lineRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        LineResponse line = lineService.addSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @DeleteMapping(value = "/{id}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity handleLineNotFoundException(LineNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(LineNameDuplicatedException.class)
    public ResponseEntity handleLineNameDuplicatedException(LineNameDuplicatedException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NewUpStationIsWrongException.class)
    public ResponseEntity handleNewUpStationIsWrongException(NewUpStationIsWrongException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NewDownStationIsAlreadyRegisteredException.class)
    public ResponseEntity handleNewDownStationIsAlreadyRegisteredException(NewDownStationIsAlreadyRegisteredException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DeleteStationIsNotLastStationException.class)
    public ResponseEntity handleDeleteStationIsNotLastStationException(DeleteStationIsNotLastStationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OnlyOneSectionLeftCannotBeDeletedException.class)
    public ResponseEntity handleOnlyOneSectionLeftCannotBeDeletedException(OnlyOneSectionLeftCannotBeDeletedException e) {
        return ResponseEntity.badRequest().build();
    }
}
