package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("lines")
public class LineController {
    private final LineFacadeService lineFacadeService;
    public LineController(LineFacadeService lineFacadeService) {
        this.lineFacadeService = lineFacadeService;
    }


    @PostMapping
    public ResponseEntity<LineResponse> create(@RequestBody LineRequest request) {
        LineResponse line = lineFacadeService.create(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                .body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getList() {
        return ResponseEntity.ok()
                .body(lineFacadeService.getList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(lineFacadeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> update(@PathVariable Long id, @RequestBody LineRequest request) {
        lineFacadeService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        lineFacadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
