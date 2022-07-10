package nextstep.subway.ui;

import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.stationLine.CreateStationLineRequest;
import nextstep.subway.applicaion.dto.stationLine.CreateStationLineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationLineController {
    private final StationLineService stationLineService;

    public StationLineController(final StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<CreateStationLineResponse> createStationLine(@RequestBody CreateStationLineRequest request){
        CreateStationLineResponse response = stationLineService.saveStationLine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
