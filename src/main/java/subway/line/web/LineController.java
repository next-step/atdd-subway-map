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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/lines/{id}/sections")
    Long addSection(@PathVariable Long id, @RequestBody AddSectionRequest request) {
        return lineService.addSection(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/lines/{id}/sections")
    void removeSection(@PathVariable Long id, @RequestParam Long sectionId) {
        lineService.removeSection(id, sectionId);
    }

}
