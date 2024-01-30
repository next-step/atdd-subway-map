package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.create.LineCreateRequest;
import subway.line.create.LineCreateService;
import subway.line.create.LineCreatedResponse;
import subway.line.load.LineLoadService;
import subway.line.load.LineLoadedResponse;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineCreateService lineCreateService;
    private final LineLoadService lineLoadService;

    public LineController(LineCreateService lineCreateService, LineLoadService lineLoadService) {
        this.lineCreateService = lineCreateService;
        this.lineLoadService = lineLoadService;
    }

    @PostMapping
    public ResponseEntity<LineCreatedResponse> createLine(@RequestBody LineCreateRequest request) {
        LineCreatedResponse response = lineCreateService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LineLoadedResponse>> getLine() {
        List<LineLoadedResponse> responses = lineLoadService.getLines();
        return ResponseEntity.ok().body(responses);
    }
}
