package subway.stationline;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.stationline.dto.StationLineCreateRequest;
import subway.stationline.dto.StationLineCreateResponse;
import subway.stationline.dto.StationLineReadListResponse;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {
    private StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineCreateResponse> craeteStation(@RequestBody StationLineCreateRequest request) {
        StationLineCreateResponse response = stationLineService.createStationLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineReadListResponse>> readStationLineList() {
        return ResponseEntity.ok(stationLineService.readStationLineList());
    }



}
