package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.service.LineService;
import subway.service.StationService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;
    private StationService stationService;

    public LineController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/stations/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.findLines();
        return ResponseEntity.ok().body(lineResponses);
    }

}
