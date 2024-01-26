package subway;

import java.util.Optional;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }
    @GetMapping(value = "/stations/{id}")
    public ResponseEntity<Optional<StationResponse>> showStations(@PathVariable Long id) {
        return ResponseEntity.ok().body(stationService.findStation(id));
    }

    @PutMapping("/stations/{id}")
    public ResponseEntity<StationResponse> createStation(
        @PathVariable Long id,
        @RequestBody StationRequest stationRequest
    ) throws NotFoundException {
        StationResponse station = stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().body(station);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
