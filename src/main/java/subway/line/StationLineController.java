package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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
    public ResponseEntity<StationLineResponse> createStationLines(@RequestBody StationLineRequest stationLineRequest) {
        StationLineResponse stationLines = stationLineService.createStationLines(stationLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + stationLines.getId())).body(stationLines);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> getStationLines() {
        return ResponseEntity.ok().body(stationLineService.findAllStations());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<StationLineResponse> getStationLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(stationLineService.findOneStation(id));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<StationLineResponse> deleteStationLine(@PathVariable Long id) {
        stationLineService.deleteStationLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> putStationLine(@PathVariable Long id,
                                               @RequestBody StationsLineUpdateRequest stationsLineUpdateRequest) {
        stationLineService.putStationLineById(id, stationsLineUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
