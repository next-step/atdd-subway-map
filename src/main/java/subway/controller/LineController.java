package subway.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.model.Line;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok().body(lineService.findALlLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        Line line = lineService.findLineById(id);
        return ResponseEntity.ok().body(lineService.createLineResponse(line));
    }
}
