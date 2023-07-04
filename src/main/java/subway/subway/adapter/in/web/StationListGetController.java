package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.subway.application.in.StationListGetQuery;
import subway.subway.application.query.StationResponse;

import java.util.List;

@RestController
class StationListGetController {

    private final StationListGetQuery stationListGetQuery;

    public StationListGetController(StationListGetQuery stationListGetQuery) {
        this.stationListGetQuery = stationListGetQuery;
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationListGetQuery.findAllStations());
    }
}
