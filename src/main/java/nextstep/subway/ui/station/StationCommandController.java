package nextstep.subway.ui.station;

import java.net.URI;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.applicaion.station.StationModifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stations")
public class StationCommandController {
    private final StationModifyService stationModifyService;

    public StationCommandController(StationModifyService stationModifyService) {
        this.stationModifyService = stationModifyService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(
            @RequestBody StationRequest stationRequest) {
        StationResponse station = stationModifyService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationModifyService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
