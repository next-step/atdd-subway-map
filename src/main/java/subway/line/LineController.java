package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<Line> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        Line result = lineService.createLine(lineCreateRequest);

        return ResponseEntity
                .created(URI.create(format("/lines/%s", result.getId())))
                .body(result);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<Line>> getLines() {
        return ResponseEntity.ok(lineService.getLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<Line> getLine(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.getLineById(id));
    }
}
