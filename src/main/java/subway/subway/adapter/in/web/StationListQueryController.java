package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.subway.application.in.StationListQuery;
import subway.subway.application.query.StationResponse;

import java.util.List;

@RestController
class StationListQueryController {

    private final StationListQuery stationListQuery;

    public StationListQueryController(StationListQuery stationListQuery) {
        this.stationListQuery = stationListQuery;
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationListQuery.findAll());
    }
}
