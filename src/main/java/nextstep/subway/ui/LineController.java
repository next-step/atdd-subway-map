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
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/subway-lines")
    public ResponseEntity<LineResponse> createSubwayLine(@RequestBody CreateLineRequest subwayLineRequest) {
        final LineResponse lineResponse = lineService.saveSubwayLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/stations/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/subway-lines")
    public ResponseEntity<List<LineResponse>> getSubwayLines() {
        final List<LineResponse> subwayLinesResponse = lineService.findAllSubwayLines();
        return ResponseEntity.ok().body(subwayLinesResponse);
    }

    @GetMapping("/subway-lines/{subwayLineId}")
    public ResponseEntity<LineResponse> getSubwayLine(@PathVariable Long subwayLineId) {
        final LineResponse lineResponse = lineService.findOneSubwayLineById(subwayLineId);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping("/subway-lines/{subwayLineId}")
    public ResponseEntity<LineResponse> updateSubwayLine(
            @PathVariable Long subwayLineId,
            @RequestBody UpdateLineRequest updateLineRequest) {
        final LineResponse lineResponse = lineService.updateSubwayLine(subwayLineId, updateLineRequest);
        return ResponseEntity.ok().body(lineResponse);
    }

    @DeleteMapping("/subway-lines/{subwayLineId}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long subwayLineId) {
        lineService.performDeleteSubwayLine(subwayLineId);
        lineService.deleteSubwayLine(subwayLineId);
        return ResponseEntity.noContent().build();
    }
}
