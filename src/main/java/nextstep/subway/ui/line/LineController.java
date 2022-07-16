package nextstep.subway.ui.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.line.LineService;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> showLines(@PathVariable final Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable final Long id,
            @RequestBody final LineRequest request
    ) {
        lineService.updateLineById(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
