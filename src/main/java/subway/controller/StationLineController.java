package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.request.StationLineRequest;
import subway.controller.resonse.StationLineResponse;
import subway.controller.resonse.StationResponse;
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
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "언주역"));
        StationLineResponse response = new StationLineResponse(1L, "신분당선", "bg-red-600", stationResponses);
        return ResponseEntity.ok().body(List.of(response));
    }
}
