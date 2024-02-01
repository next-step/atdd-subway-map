package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationSectionService;
import subway.dto.StationSectionRequest;
import subway.dto.StationSectionResponse;
import subway.entity.StationSection;

import java.net.URI;
import java.util.Map;

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
                stationLineService.saveStationSection(request.updateStationLineId(stationLineId));
        return ResponseEntity.created(
                URI.create(String.format("/lines/%d/sections", stationLineId))).body(stationSectionResponse);
    }
}
