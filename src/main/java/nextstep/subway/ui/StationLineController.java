package nextstep.subway.ui;

import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineModifyRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationLineSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {

    public static final String STATION_LINE_URI = "/station/lines";
    public static final String STATION_LINE_BY_ID_URI = "/station/lines/{id}";

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @GetMapping(STATION_LINE_URI)
    public ResponseEntity<List<StationLineResponse>> getStationLines() {
        return ResponseEntity.ok(stationLineService.getStationLines());
    }

    @GetMapping(STATION_LINE_BY_ID_URI)
    public ResponseEntity<StationLineResponse> getStationLine(@PathVariable long id) {
        return ResponseEntity.ok(stationLineService.getStationLineById(id));
    }

    @PostMapping(STATION_LINE_URI)
    public ResponseEntity<Void> saveStationLine(@RequestBody StationLineSaveRequest stationLineSaveRequest) {
        StationLineResponse response = stationLineService.saveStationLine(stationLineSaveRequest);
        return ResponseEntity.created(URI.create(STATION_LINE_URI + response.getId())).build();
    }

    @PutMapping(STATION_LINE_URI)
    public ResponseEntity<Void> modifyStationLine(@RequestBody StationLineModifyRequest stationLineModifyRequest) {
        stationLineService.modifyStationLine(stationLineModifyRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(STATION_LINE_BY_ID_URI)
    public ResponseEntity<Void> deleteStationLine(@PathVariable long id) {
        stationLineService.deleteStationLine(id);
        return ResponseEntity.noContent().build();
    }
}
