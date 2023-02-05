package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.station.CreateStationRequest;
import subway.dto.station.CreateStationResponse;
import subway.dto.station.ReadStationListResponse;
import subway.dto.station.ReadStationResponse;
import subway.service.StationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    @PostMapping("")
    public ResponseEntity<CreateStationResponse> createStation(@RequestBody CreateStationRequest createStationRequest) {
        CreateStationResponse station = stationService.createStation(createStationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + station.getId()))
                .body(station);
    }

    @GetMapping("")
    public ResponseEntity<List<ReadStationListResponse>> readStationList() {
        return ResponseEntity
                .ok()
                .body(stationService.readStationList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStationResponse> readStation(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(stationService.readStation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }

}
