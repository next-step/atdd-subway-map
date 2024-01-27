package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationLineService;
import subway.entity.StationLine;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class StationLineController {

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLine> createStationLine(@RequestBody Map<String, String> map) {
        StationLine stationLine = stationLineService.saveStationLine(map);
        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).build();
    }

    @GetMapping("lines")
    public ResponseEntity<List<StationLine>> findAllStationLines() {
        return ResponseEntity.ok(stationLineService.findAllStationLines());
    }
}
