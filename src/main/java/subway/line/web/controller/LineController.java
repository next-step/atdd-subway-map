package subway.line.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.business.LineService;
import subway.line.business.model.Line;
import subway.line.web.dto.LineCreateRequest;
import subway.line.web.dto.LineCreateResponse;
import subway.station.StationResponse;
import subway.station.StationService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
public class LineController {

    private final LineService lineService;
    private final StationService stationService;

    @PostMapping("lines")
    public ResponseEntity<LineCreateResponse> createLine(@RequestBody LineCreateRequest request) {
        Line newLine = lineService.create(request.toLine());
        StationResponse upStation = stationService.findStation(request.getUpStationId());
        StationResponse downStation = stationService.findStation(request.getDownStationId());

        return ResponseEntity.created(URI.create("/lines/"+newLine.getId())).body(new LineCreateResponse(newLine, List.of(upStation, downStation)));
    }

}
