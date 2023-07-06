package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.ok(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }
    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.getLine(lineId));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<?> updateLine(@PathVariable Long lineId, @RequestBody LineRequest request) {
        lineService.updateLine(lineId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{lineId}")
    public ResponseEntity<?> deleteStationLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);

        return ResponseEntity.ok().build();
    }

}
