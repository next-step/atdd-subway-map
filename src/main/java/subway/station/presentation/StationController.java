package subway.station.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.application.StationService;
import subway.station.application.dto.request.StationRequest;
import subway.station.application.dto.response.StationResponse;
import subway.station.domain.Station;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    ResponseEntity<StationResponse> createStation(@RequestBody final StationRequest stationRequest) {
        Long stationId = stationService.saveStation(stationRequest);
        Station findStation = stationService.findStationById(stationId);

        return ResponseEntity.created(URI.create("/stations/" + stationId))
                .body(StationResponse.from(findStation));
    }

    @GetMapping
    ResponseEntity<List<StationResponse>> showStations() {
        List<Station> findStations = stationService.findAllStations();

        return ResponseEntity.ok()
                .body(StationResponse.fromList(findStations));
    }

    @DeleteMapping("/{stationId}")
    ResponseEntity<Void> deleteStation(@PathVariable final Long stationId) {
        stationService.deleteStationById(stationId);

        return ResponseEntity.noContent().build();
    }
}
