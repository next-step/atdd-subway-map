package nextstep.subway.ui;

import java.net.URI;
import nextstep.subway.applicaion.StationSectionService;
import nextstep.subway.applicaion.dto.StationSectionRequest;
import nextstep.subway.applicaion.dto.StationSectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationSectionController {

    private final StationSectionService stationSectionService;

    public StationSectionController(StationSectionService stationSectionService) {
        this.stationSectionService = stationSectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<StationSectionResponse> createStationSection(
            @PathVariable Long lineId,
            @RequestBody StationSectionRequest request
    ) {
        StationSectionResponse stationLine = stationSectionService.createStationSection(request, lineId);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/section/" + stationLine.getId()))
                .body(stationLine);
    }
}
