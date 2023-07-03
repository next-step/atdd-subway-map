package subway.line;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.Station;
import subway.station.StationService;

@RestController
public class LineController {

    private final LineService lineService;
    private final StationService stationService;

    public LineController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @PostMapping("/lines")
    public ResponseEntity<Line> createLine(@RequestBody LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());
        Line line = lineService.saveLine(lineRequest.toEntity(List.of(upStation, downStation)));
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                .header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)
                .body(line);
    }
}
