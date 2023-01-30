package subway.stationline;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.stationline.dto.*;

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

    @GetMapping("/lines/{stationLineId}")
    public ResponseEntity<StationLineReadResponse> readStationLine(@PathVariable Long stationLineId) {
        return ResponseEntity.ok(stationLineService.readStationLine(stationLineId));
    }

    @PutMapping("/lines/{stationLineId}")
    public ResponseEntity<Void> readStationLine(@PathVariable Long stationLineId, @RequestBody StationLineUpdateRequest request) {
        stationLineService.updateStationLine(stationLineId, request);
        return ResponseEntity.ok().build();
    }


}
