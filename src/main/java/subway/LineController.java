package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    @PostMapping()
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest request) {
        long tempId = 1L;
        return ResponseEntity.created(URI.create("/lines/" + tempId)).body(
                new CreateLineResponse(
                        request.getName(),
                        request.getColor(),
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()
                )
        );
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> response = new ArrayList<>();
        response.add(new LineResponse(
                1L,
                "1호선",
                "남색",
                new ArrayList<>()
        ));
        return ResponseEntity.ok().body(response);
    }
}
