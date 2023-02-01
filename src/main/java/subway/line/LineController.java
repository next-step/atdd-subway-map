package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static subway.line.LineController.LINE_URI_PATH;

@RestController
@RequestMapping(LINE_URI_PATH)
public class LineController {
    public static final String LINE_URI_PATH = "/lines";

    private final LineSectionService lineSectionService;

    public LineController(LineSectionService lineSectionService) {
        this.lineSectionService = lineSectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineSectionService.saveLineSection(lineRequest);
        return ResponseEntity.created(URI.create(LINE_URI_PATH + "/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineSectionService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineSectionService.getLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id,
                                                   @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok().body(lineSectionService.updateLine(id, lineRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineSectionService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
