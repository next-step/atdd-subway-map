package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineStationController {

    LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity createLineStation(@PathVariable Long lineId, @RequestBody LineStationRequest lineStationRequest) {
        lineStationService.addLineStation(lineId, lineStationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
