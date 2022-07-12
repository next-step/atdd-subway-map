package nextstep.subway.ui;

import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {

    StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest stationLineRequest) {
        StationLineResponse stationLine = stationLineService.saveStationLine(stationLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).body(stationLine);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationLineResponse>> getStationLines() {
        return ResponseEntity.ok().body(stationLineService.findAllStationLines());
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StationLineResponse> getStationLine(@PathVariable Long id){
        return ResponseEntity.ok().body(stationLineService.findStationLine(id));
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<Void> updateStationLine(@PathVariable Long id, @RequestBody StationLineRequest stationLineRequest){
        stationLineService.updateStationLine(id, stationLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
        stationLineService.deleteStationLineById(id);
        return ResponseEntity.noContent().build();
    }
}
