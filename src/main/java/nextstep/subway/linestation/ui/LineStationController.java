package nextstep.subway.linestation.ui;

import nextstep.subway.linestation.application.LineStationService;
import nextstep.subway.linestation.application.exception.StationNotFoundException;
import nextstep.subway.linestation.dto.LineStationRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping
    public ResponseEntity<?> registerStationToLine(@PathVariable long lineId, @RequestBody LineStationRequest lineStationRequest) {
        lineStationService.registerStationToLine(lineId, lineStationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<?> removeStationFromLine(@PathVariable long lineId, @PathVariable long stationId) {
        lineStationService.removeStationFromLine(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler({DataIntegrityViolationException.class, StationNotFoundException.class})
    public ResponseEntity<?> handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
