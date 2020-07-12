package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity addStation(@PathVariable Long lineId, @RequestBody LineStationRequest lineStationRequest) {
        lineStationService.addStation(lineId, lineStationRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity removeStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineStationService.removeStation(lineId, stationId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
