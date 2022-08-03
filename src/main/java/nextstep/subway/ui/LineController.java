package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.line.CreateLineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.line.UpdateLineRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("")
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest subwayLineRequest) {
        final LineResponse lineResponse = lineService.saveLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/stations/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("")
    public ResponseEntity<List<LineResponse>> getLines() {
        final List<LineResponse> subwayLinesResponse = lineService.findAllLines();
        return ResponseEntity.ok().body(subwayLinesResponse);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId) {
        final LineResponse lineResponse = lineService.findOneLineById(lineId);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(
            @PathVariable Long lineId,
            @RequestBody UpdateLineRequest updateLineRequest) {
        final LineResponse lineResponse = lineService.updateSubwayLine(lineId, updateLineRequest);
        return ResponseEntity.ok().body(lineResponse);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.performDeleteLine(lineId);
        lineService.deleteSubwayLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
