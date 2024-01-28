package subway.line;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationLineController {
    private StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest stationLineRequest) {
        StationLineResponse station = stationLineService.saveStationLine(stationLineRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<StationLineResponse> showStationLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(stationLineService.findStationLineById(id));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> showStationLines() {
        return ResponseEntity.ok().body(stationLineService.findAllStationLine());
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<StationLineResponse> updateStationLine(@PathVariable Long id, @RequestBody StationLineRequest stationLineRequest) {
        stationLineService.updateStationLineById(id, stationLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
        stationLineService.deleteStationLineById(id);
        return ResponseEntity.noContent().build();
    }
}
