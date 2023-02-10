package subway.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.models.Station;
import subway.services.StationService;

@RestController
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(
        @RequestBody StationRequest stationRequest) {
        Station station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId()))
            .body(StationResponse.of(station));
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(StationResponse.of(stationService.findAllStations()));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
