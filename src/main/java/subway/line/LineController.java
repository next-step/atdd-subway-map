package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
        LineResponse lineResponse = LineResponse.from(lineService.saveLine(request));
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping(value = "/lines")
    ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines().stream().map(LineResponse::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/lines/{id}")
    ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(LineResponse.from(lineService.findLineById(id)));
    }

    @PutMapping(value = "/lines/{id}")
    ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody UpdateLineRequest request) {
        lineService.updateLineById(id, request);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(value = "/lines/{id}")
    ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
