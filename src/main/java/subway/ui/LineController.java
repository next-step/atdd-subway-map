package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.dto.AddSectionRequest;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.UpdateLineRequest;

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
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        final LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
                .body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        final List<LineResponse> lineResponses = lineService.findAllLine();
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable final Long lineId) {
        final LineResponse lineResponse = lineService.findLineById(lineId);
        return ResponseEntity.ok(lineResponse);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(
            @PathVariable final Long lineId,
            @RequestBody final UpdateLineRequest updateLineRequest) {
        final LineResponse lineResponse = lineService.updateLine(lineId, updateLineRequest);
        return ResponseEntity.ok(lineResponse);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> addSection(
            @PathVariable final Long lineId,
            @RequestBody final AddSectionRequest addSectionRequest) {
        final LineResponse lineResponse = lineService.addSection(lineId, addSectionRequest);
        return ResponseEntity.ok(lineResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeSection(
            @PathVariable final Long lineId,
            @RequestParam final Long stationId) {
        lineService.removeSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
