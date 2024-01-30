package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.create.LineCreateRequest;
import subway.line.create.LineCreateService;
import subway.line.create.LineCreatedResponse;
import subway.line.load.LineLoadService;
import subway.line.load.LineLoadedResponse;
import subway.line.update.LineUpdateRequest;
import subway.line.update.LineUpdateService;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineCreateService lineCreateService;
    private final LineLoadService lineLoadService;
    private final LineUpdateService lineUpdateService;

    public LineController(LineCreateService lineCreateService, LineLoadService lineLoadService, LineUpdateService lineUpdateService) {
        this.lineCreateService = lineCreateService;
        this.lineLoadService = lineLoadService;
        this.lineUpdateService = lineUpdateService;
    }

    @PostMapping
    public ResponseEntity<LineCreatedResponse> createLine(@RequestBody LineCreateRequest request) {
        LineCreatedResponse response = lineCreateService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LineLoadedResponse>> getLine() {
        List<LineLoadedResponse> responses = lineLoadService.loadLines();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{line-id}")
    public ResponseEntity<LineLoadedResponse> getLine(@PathVariable("line-id") Long lineId) {
        LineLoadedResponse response = lineLoadService.getLine(lineId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{line-id}")
    public ResponseEntity<LineLoadedResponse> updateLine(@PathVariable("line-id") Long lineId, @RequestBody LineUpdateRequest request) {
        lineUpdateService.updateLine(lineId, request);
        return ResponseEntity.ok().build();
    }
}
