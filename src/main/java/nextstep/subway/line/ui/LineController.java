package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.*;
import nextstep.subway.line.exception.DuplicateLineException;
import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.line.exception.NoSuchStationException;
import nextstep.subway.line.exception.NoSuchLineException;
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
    public ResponseEntity<CreatedLineResponse> createLine(@RequestBody LineRequest lineRequest) {
        CreatedLineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> getLineById(@PathVariable("lineId") Long lineId) {
        LineResponse line = lineService.getLine(lineId);
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

    @PostMapping(value = "/{lineId}/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectionResponse> appendSection(@PathVariable("lineId") Long lineId, @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = lineService.saveSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(section);
    }

    @DeleteMapping(value = "/{lineId}/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSection(@PathVariable("lineId") Long lineId, @RequestParam("stationId") Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(DuplicateLineException.class)
    public ResponseEntity handleDuplicateLineException(DuplicateLineException e) {
        HashMap<String, String> resultBody = new HashMap();
        resultBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(resultBody);
    }

    @ExceptionHandler(NoSuchStationException.class)
    public ResponseEntity handleNoSuchStationException(NoSuchStationException e) {
        HashMap<String, String> resultBody = new HashMap();
        resultBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(resultBody);
    }

    @ExceptionHandler(NoSuchLineException.class)
    public ResponseEntity handleNoSuchLineException(NoSuchLineException e) {
        HashMap<String, String> resultBody = new HashMap();
        resultBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(resultBody);
    }

    @ExceptionHandler(InvalidSectionException.class)
    public ResponseEntity handleInvalidSectionException(InvalidSectionException e) {
        HashMap<String, String> resultBody = new HashMap();
        resultBody.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(resultBody);
    }
}
