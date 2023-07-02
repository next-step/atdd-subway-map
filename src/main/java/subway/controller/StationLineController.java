package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.request.StationLineRequest;
import subway.controller.resonse.StationLineResponse;
import subway.service.StationLineService;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest stationLineRequest) {
        StationLineResponse stationLineResponse = stationLineService.saveStationLine(stationLineRequest);
        return ResponseEntity.created(URI.create("/stations/" + 1)).body(stationLineResponse);
    }


    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> showStationLines() {
        return ResponseEntity.ok().body(stationLineService.findAllStationLines());
    }
}
