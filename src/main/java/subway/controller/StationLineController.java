package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.request.StationLineRequest;
import subway.controller.resonse.StationLineResponse;
import subway.controller.resonse.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {


    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest stationRequest) {
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "언주역"));
        StationLineResponse response = new StationLineResponse(1L, stationRequest.getLineName(), stationRequest.getColor(), stationResponses);
        return ResponseEntity.created(URI.create("/stations/" + 1)).body(response);
    }


    @GetMapping("/lines")
    public ResponseEntity<StationLineResponse> showStationLines() {
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "언주역"));
        StationLineResponse response = new StationLineResponse(1L, "신분당선", "bg-red-600", stationResponses);
        return ResponseEntity.ok().body(response);
    }
}
