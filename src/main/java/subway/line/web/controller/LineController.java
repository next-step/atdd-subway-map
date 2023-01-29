package subway.line.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.business.model.Line;
import subway.line.business.service.LineService;
import subway.line.web.dto.LineRequest;
import subway.line.web.dto.LineResponse;
import subway.station.StationResponse;
import subway.station.StationService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class LineController {

    private final LineService lineService;
    private final StationService stationService;

    @PostMapping("lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        Line newLine = lineService.create(request.toLine());
        return ResponseEntity.created(URI.create("/lines/"+newLine.getId())).body(createResponse(newLine));
    }

    @GetMapping("lines")
    public ResponseEntity<List<LineResponse>> getAllLines() {
        List<LineResponse> response = lineService.getAllLines().stream().map(this::createResponse).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        Line line = lineService.getLine(id);
        return ResponseEntity.ok().body(createResponse(line));
    }

    private LineResponse createResponse(Line line) {
        StationResponse upStation = stationService.findStation(line.getUpStationId());
        StationResponse downStation = stationService.findStation(line.getDownStationId());

        return new LineResponse(line, List.of(upStation, downStation));
    }

}
