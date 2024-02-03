package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;
import subway.service.LineService;
import subway.dto.line.LineUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse response = lineService.createLine(request);

        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> responses = lineService.getLines();

        return ResponseEntity
                    .ok()
                    .body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineResponse response = lineService.getLine(id);

        return ResponseEntity
                    .ok()
                    .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(
        @PathVariable Long id,
        @RequestBody LineUpdateRequest request
    ) {
        lineService.modifyLine(id, request);

        return ResponseEntity
                    .ok()
                    .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);

        return ResponseEntity
                    .noContent()
                    .build();
    }
}
