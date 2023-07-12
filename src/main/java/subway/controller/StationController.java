package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.station.StationRequest;
import subway.controller.dto.station.StationResponse;
import subway.service.StationCompositeService;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private final StationCompositeService stationCompositeService;

    public StationController(StationCompositeService stationCompositeService) {
        this.stationCompositeService = stationCompositeService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationCompositeService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationCompositeService.findAllStations());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationCompositeService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
