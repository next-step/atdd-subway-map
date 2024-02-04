package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationSectionService;
import subway.dto.StationSectionRequest;
import subway.dto.StationSectionResponse;

import java.net.URI;

@RestController
public class StationSectionController {

    private final StationSectionService stationLineService;

    public StationSectionController(StationSectionService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines/{stationLineId}/sections")
    public ResponseEntity<StationSectionResponse> createStationLine(@PathVariable Long stationLineId,
                                                                    @RequestBody StationSectionRequest request) {
        StationSectionResponse stationSectionResponse =
                stationLineService.createStationSection(StationSectionRequest.mergeForCreateLine(stationLineId, request));
        return ResponseEntity.created(
                URI.create(String.format("/lines/%d/sections", stationLineId))).body(stationSectionResponse);
    }

    @DeleteMapping("/lines/{stationLineId}/sections")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long stationLineId,
                                                  @RequestParam(name = "stationId") Long stationIdToDelete) {
        stationLineService.deleteStationSection(stationLineId, stationIdToDelete);
        return ResponseEntity.noContent().build();
    }
}
