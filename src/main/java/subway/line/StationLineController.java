package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * StationLineController
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@RestController
public class StationLineController {

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLines(@RequestBody StationLineCreateRequest stationLineCreateRequest) {
        return ResponseEntity.ok().body(stationLineService.createStationLines(stationLineCreateRequest));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> getStationLines() {
        return ResponseEntity.ok().body(stationLineService.findAllStations());
    }
}
