package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.service.LineService;
import subway.line.request.LineCreateRequest;
import subway.line.response.LineResponse;
import subway.line.request.LineUpdateRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping()
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineCreateRequest lineCreateRequest) {
        LineResponse lineResponse = lineService.saveLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok().body(lineService.findAllLine());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLines(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> updateLines(@PathVariable Long id,@Valid @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }
}
