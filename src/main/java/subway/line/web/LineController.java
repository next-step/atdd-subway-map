package subway.line.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.repository.Line;
import subway.line.service.LineService;

import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/lines")
    LineResponse createLine(@RequestBody CreateLineRequest request) {
        return lineService.createLine(request);
    }

    @GetMapping("/lines")
    List<LineResponse> getLines() {
        return lineService.getLines();
    }

    @GetMapping("/lines/{id}")
    LineResponse getLines(@PathVariable Long id) {
        return lineService.getLine(id);
    }

    @PutMapping("/lines/{id}")
    void updateLine(@PathVariable Long id, @RequestBody UpdateLineRequest request) {
        lineService.updateLine(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/lines/{id}")
    void deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
    }

    @PostMapping("/lines/{id}/sections")
    ResponseEntity<?> addSection(@PathVariable Long id, @RequestBody AddSectionRequest request) {
        try {
            Long sectionId = lineService.addSection(id, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(sectionId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/lines/{id}/sections")
    ResponseEntity<?> removeSection(@PathVariable Long id, @RequestParam Long sectionId) {
        try {
            lineService.removeSection(id, sectionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
