package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.line.*;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("")
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest request) {
        CreateLineResponse response = lineService.createLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<ReadLinesResponse>> readLines() {
        return ResponseEntity
                .ok(lineService.readLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadLineResponse> readLine(@PathVariable Long id) {
        return ResponseEntity
                .ok(lineService.readLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id,
                                           @RequestBody UpdateLineRequest request) {
        lineService.updateLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity
                .noContent()
                .build();
    }



}
