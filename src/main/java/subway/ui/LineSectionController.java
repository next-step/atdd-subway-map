package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineSectionService;
import subway.ui.dto.LineSectionRequest;
import subway.ui.dto.LineSectionResponse;

@RestController
@RequestMapping("/lines")
public class LineSectionController {

    private final LineSectionService lineSectionService;

    public LineSectionController(final LineSectionService lineSectionService) {
        this.lineSectionService = lineSectionService;
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> addSection(@PathVariable final Long id, @RequestBody final LineSectionRequest lineSectionRequest) {

        lineSectionService.addSection(id, lineSectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> removeLine(@PathVariable final Long id, @RequestParam final Long stationId) {

        lineSectionService.removeSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sections")
    public ResponseEntity<LineSectionResponse> findSectionByLine(@PathVariable final Long id) {

        return ResponseEntity.ok().body(lineSectionService.findSectionByline(id));
    }
}
