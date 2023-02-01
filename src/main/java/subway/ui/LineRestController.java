package subway.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.service.LineService;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class LineRestController {

    private final LineService lineService;

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAll() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LineResponse> save(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.update(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
