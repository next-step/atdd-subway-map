package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationDto;
import subway.service.StationService;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StationController {
    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationDto stationDto = stationService.saveStation(stationRequest.toDto());
        return ResponseEntity.created(URI.create("/stations/" + stationDto.getId())).body(StationResponse.from(stationDto));
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResponse> responses = stationService.findAllStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
