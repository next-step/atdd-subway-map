package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StationLineController {
    private final StationLineService stationLineService;

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> cretaeStationLine(@RequestBody StationLineRequest stationLineRequest) {
        StationLineResponse stationLineResponse = stationLineService.createStationLine(stationLineRequest);
        return ResponseEntity.created(URI.create("/lines" + stationLineResponse.getId())).body(stationLineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<StationLineResponse>> findAllStationLine() {
        return ResponseEntity.ok(stationLineService.findAllStationLine());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<StationLineResponse> findByStationLineId(@PathVariable Long id) {
        return ResponseEntity.ok(stationLineService.findByStationLineId(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateByStationLineId(@PathVariable Long id,
                                                @RequestBody StationLineRequest stationLineRequest) {
        stationLineService.updateByStationLineId(id, stationLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteByStationLineId(@PathVariable Long id) {
        stationLineService.deleteByStationLineId(id);
        return ResponseEntity.noContent().build();
    }

}
