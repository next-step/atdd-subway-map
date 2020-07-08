package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines/{id}/stations")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping
    public ResponseEntity<LineStationResponse> addLineStation(@PathVariable Long id,
                                                          @RequestBody LineStationRequest lineStationRequest) {
        LineStationResponse response = this.lineStationService.addLineStation(id, lineStationRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/stations")).body(response);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<LineStationResponse> removeLineStation(@PathVariable Long id, @PathVariable Long stationId) {
        this.lineStationService.removeLineStation(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
