package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @GetMapping
    public ResponseEntity<List<LineStationResponse>> findAllLineStations(@PathVariable Long lineId) {
        List<LineStationResponse> lineStationResponses = lineStationService.findAll(lineId);
        return ResponseEntity.ok().body(lineStationResponses);
    }

    @PostMapping
    public ResponseEntity<LineStationResponse> createLineStation(@PathVariable Long lineId, @RequestBody LineStationRequest createLineStationRequest) {

        LineStationResponse newLineStationResponse = lineStationService.addLineStation(lineId, createLineStationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{stationId}")
                .buildAndExpand(newLineStationResponse.getStation().getId())
                .toUri();

        return ResponseEntity.created(location).body(newLineStationResponse);
    }
}
