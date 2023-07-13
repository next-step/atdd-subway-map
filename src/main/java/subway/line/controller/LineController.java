package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.AddLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.ModifyLineResponse;
import subway.line.service.LineService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody AddLineRequest addLineRequest) {
        LineResponse createdLine = lineService.createLine(addLineRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLine);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineResponse line = lineService.getLine(id);
        return ResponseEntity.ok(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<ModifyLineResponse> modifyLine(@PathVariable Long id, @RequestBody ModifyLineRequest modifyLineRequest) {
        ModifyLineResponse modifiedLine = lineService.modifyLine(id, modifyLineRequest);
        return ResponseEntity.ok(modifiedLine);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }
}
