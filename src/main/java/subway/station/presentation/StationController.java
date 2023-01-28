package subway.station.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.presentation.dto.request.StationRequest;
import subway.station.presentation.dto.response.StationResponse;
import subway.station.application.StationService;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody final StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping("/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
