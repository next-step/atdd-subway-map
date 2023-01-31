package subway.presentation.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.line.LineFacade;
import subway.presentation.line.dto.request.LineRequest;
import subway.presentation.line.dto.response.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineFacade lineFacade;

    @PostMapping("")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse line = lineFacade.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("")
    public ResponseEntity<List<LineResponse>> getAllLines() {
        return ResponseEntity.ok(lineFacade.getAllLines());
    }


    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineFacade.getLine(lineId));
    }
}