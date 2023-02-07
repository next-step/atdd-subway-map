package subway.controller.line;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.line.dto.CreateLineRequest;
import subway.controller.line.dto.LineResponse;
import subway.controller.line.dto.UpdateLineRequest;
import subway.controller.line.dto.AddSectionRequest;
import subway.service.LineService;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
        LineResponse response = lineService.saveLine(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLineList() {
        List<LineResponse> response = lineService.findAllLines();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable(name = "id") Long id) {
        LineResponse response = lineService.findLineById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable(name = "id") Long id, @RequestBody UpdateLineRequest request) {
        lineService.updateLineById(id, request);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable(name = "id") Long id) {
        lineService.deleteLineById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity addSection(
            @PathVariable(name = "id") Long id, @RequestBody AddSectionRequest request) {
        lineService.addSection(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> deleteSection(
            @PathVariable(name = "id") Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }
}
