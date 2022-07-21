package nextstep.subway.lines.ui;

import nextstep.subway.lines.application.LineService;
import nextstep.subway.lines.application.dto.*;
import nextstep.subway.lines.domain.Line;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping(value = "/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineSaveResponse> saveLine(@RequestBody LineSaveRequest lineSaveRequest) {
        Line line = lineService.saveLine(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(LineSaveResponse.of(line));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok(lineService.getLines().stream()
                .map(LineResponse::new)
                .collect(toList()));
    }

    @GetMapping(value = "/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId) {
        return ResponseEntity.ok(new LineResponse(lineService.getLine(lineId)));
    }

    @PutMapping(value = "/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId,
                                           @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines")).build();
    }

    @DeleteMapping(value = "/{lineId}/sections")
    public ResponseEntity<Void> removeSection(@PathVariable Long lineId,
                                              @RequestParam Long stationId) {
        lineService.removeSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
