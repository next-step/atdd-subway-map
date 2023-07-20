package subway.station.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;
import subway.station.repository.Station;
import subway.station.service.StationService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
class StationController {
    private final StationService stationService;

    StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        Station station = stationService.saveStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + station.getId()))
                .body(new StationResponse(station.getId(), station.getName()));
    }

    @GetMapping(value = "/stations")
    ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations()
                .stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/stations/{id}")
    ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
