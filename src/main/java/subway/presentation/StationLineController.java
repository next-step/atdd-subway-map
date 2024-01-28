package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationLineService;
import subway.dto.StationLineRequest;
import subway.dto.StationLineResponse;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest request) {
        StationLineResponse stationLine = stationLineService.saveStationLine(request);
        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).body(stationLine);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> findAllStationLines() {
        return ResponseEntity.ok(stationLineService.findAllStationLines());
    }

    @GetMapping("/lines/{stationLineId}")
    public ResponseEntity<StationLineResponse> findStationLineById(@PathVariable Long stationLineId) {
        return ResponseEntity.ok(stationLineService.findStationLineById(stationLineId));
    }

    @PatchMapping("/lines/{stationLineId}")
    public ResponseEntity<StationLineResponse> updateStationLine(@PathVariable Long stationLineId,
                                                                 @RequestBody StationLineRequest request) {
        StationLineResponse stationLine = stationLineService.updateStationLine(stationLineId, request);
        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).body(stationLine);
    }

    @DeleteMapping("/lines/{stationLineId}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long stationLineId) {
        stationLineService.deleteStationLine(stationLineId);
        return ResponseEntity.noContent().build();
    }
}
