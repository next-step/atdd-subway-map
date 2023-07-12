package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.facade.LineFacade;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("lines")
public class LineController {
    private final LineFacade lineFacade;
    public LineController(LineFacade lineFacade) {
        this.lineFacade = lineFacade;
    }


    @PostMapping
    public ResponseEntity<LineResponse> create(@RequestBody LineRequest request) {
        LineResponse line = lineFacade.create(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                .body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getList() {
        return ResponseEntity.ok()
                .body(lineFacade.getList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(lineFacade.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> update(@PathVariable Long id, @RequestBody LineRequest request) {
        lineFacade.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        lineFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
