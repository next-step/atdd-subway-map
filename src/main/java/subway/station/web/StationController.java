package subway.station.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.web.dto.StationRequest;
import subway.station.web.dto.StationFindAllResponse;
import subway.station.service.StationService;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationFindAllResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationFindAllResponse station = stationService.save(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationFindAllResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
