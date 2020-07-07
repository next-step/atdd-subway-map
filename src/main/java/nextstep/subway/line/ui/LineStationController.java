package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity createLineStation(@PathVariable("lineId") Long lineId, @RequestBody LineStationCreateRequest createRequest) {
        lineStationService.createLineStation(lineId, createRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId.toString())).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity createLineStation(@PathVariable("lineId") Long lineId, @PathVariable("stationId") Long stationId) {
        lineStationService.removeStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
