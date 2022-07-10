package nextstep.subway.applicaion.line.ui;

import nextstep.subway.applicaion.line.LineService;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createStation(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

//    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<StationResponse>> showStations() {
//        return ResponseEntity.ok().body(stationService.findAllStations());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
//        stationService.deleteStationById(id);
//        return ResponseEntity.noContent().build();
//    }
}
