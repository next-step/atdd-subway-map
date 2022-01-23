package nextstep.subway.ui.station;

import java.util.List;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.applicaion.station.StationReadService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stations")
public class StationQueryController {

    private final StationReadService stationReadService;

    public StationQueryController(StationReadService stationReadService) {
        this.stationReadService = stationReadService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationReadService.findAllStations());
    }
}
