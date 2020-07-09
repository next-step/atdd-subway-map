package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity addStation(@PathVariable Long lineId, @RequestBody LineStationRequest lineStationRequest) {
        try {
            lineStationService.addStation(lineId, lineStationRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
