package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.web.request.LineCreateRequest;
import subway.web.response.LineResponse;
import subway.web.response.StationResponse;

import java.util.List;

@RestController
public class LineController {

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest stationRequest) {
        LineResponse response = new LineResponse(1L, "신분당선", "bg-red-600", List.of(new StationResponse(1L, "지하철"), new StationResponse(2L, "새로운지하역")));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
