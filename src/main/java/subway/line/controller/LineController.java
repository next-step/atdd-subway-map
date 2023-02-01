package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.controller.dto.LineModifyRequest;
import subway.line.controller.dto.LineSaveRequest;
import subway.line.controller.dto.LineResponse;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineSaveRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> readLines() {
        List<LineResponse> line = lineService.findAllLines();
        return ResponseEntity.ok().body(line);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> readLine(@PathVariable Long id) {
        LineResponse line = lineService.findLineById(id);
        return ResponseEntity.ok(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody LineModifyRequest lineModifyRequest) {
        lineService.modifyLine(id, lineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.removeLineById(id);
        return ResponseEntity.noContent().build();
    }

}