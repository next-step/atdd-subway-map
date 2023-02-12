package subway.line.section;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<Void> addSection(
            @PathVariable final Long lineId,
            @RequestBody final SectionAddRequest request
    ) {

        sectionService.addSection(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSection(
            @PathVariable final Long lineId,
            @RequestParam("stationId") final Long stationId
    ) {

        sectionService.removeSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
