package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
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

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest request) {
        StationLineResponse stationLine = stationLineService.createStationLine(request);
        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).body(stationLine);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> getStationLines() {
        List<StationLineResponse> stationLines = stationLineService.getStationLines();
        return ResponseEntity.ok().body(stationLines);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<StationLineResponse> getStationLine(@PathVariable Long lineId) {
        StationLineResponse stationLines = stationLineService.getStationLine(lineId);
        return ResponseEntity.ok().body(stationLines);
    }

    @PutMapping("/lines/{lineId}")
    public ResponseEntity<Void> updateStationLine(@PathVariable Long lineId,
                                            @RequestBody StationLineRequest request) {
        stationLineService.updateStationLine(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long lineId) {
        stationLineService.deleteStationLine(lineId);
        return ResponseEntity.noContent().build();
    }
}