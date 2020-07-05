package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.dto.LineStationCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity createLineStation(@PathVariable("lineId") Long lineId, @RequestBody LineStationCreateRequest createRequest) {
        lineStationService.createLineStation(lineId, createRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/stations")).build();
    }
}
