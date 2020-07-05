package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
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
    public ResponseEntity appendStation(@PathVariable Long lineId, @RequestBody LineStationRequest lineStationRequest) {
        lineStationService.appendStation(lineStationRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/stations")).body("{}");
    }
}
