package nextstep.subway.linestation.ui;

import nextstep.subway.linestation.application.LineStationService;
import nextstep.subway.linestation.dto.LineStationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class LineStationController {

    private final Logger log = LoggerFactory.getLogger(LineStationController.class);

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping
    public ResponseEntity<?> registerStationToLine(@PathVariable long lineId, @RequestBody LineStationRequest lineStationRequest) {
        log.debug("request line: {}", lineId);
        log.debug("request body: {}", lineStationRequest);
        lineStationService.registerStationToLine(lineId, lineStationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
