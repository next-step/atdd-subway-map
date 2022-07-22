package nextstep.subway.ui;

import nextstep.subway.applicaion.StationCommandService;
import nextstep.subway.applicaion.StationQueryService;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationQueryService stationQueryService;

    private final StationCommandService stationCommandService;

    public StationController(StationQueryService stationQueryService, StationCommandService stationCommandService) {
        this.stationQueryService = stationQueryService;
        this.stationCommandService = stationCommandService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationCommandService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationQueryService.findAllStations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationCommandService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
