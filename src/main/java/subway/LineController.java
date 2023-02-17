package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse response = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @Valid @RequestBody LineModificationRequest lineModificationRequest) {
        lineService.modify(id, lineModificationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineResponse response = lineService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long id, @Valid @RequestBody SectionRequest request) {
        LineResponse response = lineService.saveSection(id, request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
}
