package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.service.LineCompositeService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineCompositeService lineCompositeService;

    public LineController(LineCompositeService lineCompositeService) {
        this.lineCompositeService = lineCompositeService;
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok()
                             .body(lineCompositeService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        return ResponseEntity.ok()
                             .body(lineCompositeService.findById(id));
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineResponse line = lineCompositeService.saveLine(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                             .body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable Long id, @RequestBody LineModifyRequest lineModifyRequest) {
        LineResponse line = lineCompositeService.modifyLine(id, lineModifyRequest);
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineCompositeService.deleteLineById(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
