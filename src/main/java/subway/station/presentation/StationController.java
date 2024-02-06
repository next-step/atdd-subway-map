package subway.station.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.presentation.request.CreateStationRequest;
import subway.station.presentation.response.CreateStationResponse;
import subway.station.presentation.response.ShowAllStationsResponse;
import subway.station.service.StationService;

import java.net.URI;

@RestController
public class StationController {
    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<CreateStationResponse> createStation(@RequestBody CreateStationRequest createStationRequest) {
        CreateStationResponse station = stationService.saveStation(createStationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<ShowAllStationsResponse> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }

}
