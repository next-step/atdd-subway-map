package subway.section;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.StationLine;
import subway.line.StationLineRequest;
import subway.line.StationLineResponse;
import subway.line.StationLineService;
import subway.station.Station;

@RestController
public class StationSectionController {
    private StationSectionService stationSectionService;

    public StationSectionController(StationSectionService stationSectionService) {
        this.stationSectionService = stationSectionService;
    }

    /**
     * 노선에 역을 추가한다.
     * {
     *     "id": 1,
     *     "name": "신분당선",
     *     "color": "bg-red-600",
     *     "stations": [
     *         {
     *             "id": 1,
     *             "name": "A"
     *         },
     *         {
     *             "id": 2,
     *             "name": "B"
     *         }
     *     ]
     * }
     *
     */
    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<StationLineResponse> createStationSection(@PathVariable long id, @RequestBody StationSectionRequest stationSectionRequest) {
        System.out.println("StationSectionController.createStationSection");
        System.out.println("stationSectionRequest = " + stationSectionRequest);
        StationLine stationLine = stationSectionService.saveStationSection(id, stationSectionRequest);
        System.out.println("last stationLine = " + stationLine);

        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).body(
                new StationLineResponse(stationLine));
    }

    @DeleteMapping("lines/{lineId}/sections/{stationId}")
    public ResponseEntity<Void> deleteStationSection(@PathVariable long lineId, @PathVariable long stationId) {
        stationSectionService.deleteStationLineById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
