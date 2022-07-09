package nextstep.subway.ui;

import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationDto;
import nextstep.subway.ui.dto.StationRequest;
import nextstep.subway.ui.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        StationResponse station = StationResponse.of(stationDto);

        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationDto> allStationDtos = stationService.findAllStations();

        return ResponseEntity.ok().body(allStationDtos.stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList()));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
