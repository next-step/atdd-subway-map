package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createStation(@RequestBody LineRequest LineRequest) {
        LineResponse station = lineService.saveStation(LineRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @PutMapping("/lines")
    public ResponseEntity<LineResponse> updateStationById(@RequestBody LineRequest LineRequest) {
        LineResponse station = lineService.saveStation(LineRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showStations() {
        return ResponseEntity.ok().body(lineService.findAllStations());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getStationById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findById(id));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        lineService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
