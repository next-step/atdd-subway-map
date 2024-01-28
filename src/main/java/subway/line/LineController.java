package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createSubwayLine(@RequestBody LineRequest request) {
        final LineResponse subwayLine = lineService.createSubwayLine(request);

        return ResponseEntity.created(URI.create("/line/"+subwayLine.getId())).body(subwayLine);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getSubwayLines() {
        final List<LineResponse> subwayLines = lineService.getSubwayLines();

        return ResponseEntity.ok(subwayLines);
    }


}
