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
    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<StationLineResponse> createStationSection(@PathVariable long id, @RequestBody StationSectionRequest stationSectionRequest) {
        StationLine stationLine = stationSectionService.saveStationSection(id, stationSectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + stationLine.getId())).body(
                new StationLineResponse(stationLine));
    }

    @DeleteMapping("lines/{lineId}/sections/{stationId}")
    public ResponseEntity<Void> deleteStationSection(@PathVariable long lineId, @PathVariable long stationId) {
        stationSectionService.deleteStationLineById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
