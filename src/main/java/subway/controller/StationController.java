package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.StationRequest;
import subway.controller.dto.StationResponse;
import subway.service.StationService;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations/all")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @GetMapping(value = "/stations/filter")
    public ResponseEntity<List<StationResponse>> findStationsBy(@RequestParam(required = false) List<Long> ids) {
        return ResponseEntity.ok().body(
                StationResponse.listOf(stationService.findStationsBy(ids))
        );
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
