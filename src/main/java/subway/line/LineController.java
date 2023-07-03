package subway.line;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());
        LineResponse line = lineService.saveLine(
                lineRequest.toEntity(List.of(upStation, downStation)));
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                .header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)
                .body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> line = lineService.showLines();
        return ResponseEntity.ok()
                .header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)
                .body(line);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> searchById(@PathVariable Long id) {
        LineResponse line = lineService.searchById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)
                .body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id,
            @RequestBody UpdateLineRequest updateLineRequest) {
        lineService.update(id, updateLineRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS).build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.VARY, HttpHeaders.ORIGIN)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)
                .header(HttpHeaders.VARY, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS).build();
    }
}
