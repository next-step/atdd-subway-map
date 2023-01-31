package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.request.LineRequest;
import subway.controller.response.LineResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> create(@RequestBody LineRequest lineRequest) {
        final LineResponse line = lineService.create(lineRequest);

        return ResponseEntity
                .created(URI.create("/lines/" + line.getId()))
                .body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getList() {
        final List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> get(@PathVariable Long id) {
        final LineResponse line = lineService.getLine(id);
        return ResponseEntity.ok(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.update(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
