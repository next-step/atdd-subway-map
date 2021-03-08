package nextstep.subway.line.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;

@RestController
@RequestMapping(value = "/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> viewLines() {
        return ResponseEntity.ok().body(lineService.findLines());
    }

    @GetMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> viewLine(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok().body(lineService.findLine(lineId));
    }

    @PutMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLine(
        @PathVariable("lineId") Long lineId, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(lineId, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable("lineId") Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{lineId}/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> addSection(
        @PathVariable("lineId") Long lineId, @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok(lineService.addSection(lineId, lineRequest));
    }

    @DeleteMapping(value = "/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(
        @PathVariable("lineId") Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
