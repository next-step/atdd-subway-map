package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.application.StationService;

@RequestMapping("/stations")
@RestController
public class StationController {

    private final StationService stationService;
    private final String baseUrl;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
        baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody final StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create(baseUrl + "/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
