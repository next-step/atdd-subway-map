package nextstep.subway.line.ui;

import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines/{id}/station")
public class LineStationController {

    @PostMapping
    public ResponseEntity<LineStationResponse> addStation(@PathVariable Long id,
                                                          @RequestBody LineStationRequest lineStationRequest) {
        return ResponseEntity.created(URI.create("/lines/" + id + "/stations")).body(new LineStationResponse());
    }
}
