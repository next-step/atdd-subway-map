package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static subway.line.LineController.LINE_URI_PATH;

@RestController
@RequestMapping(LINE_URI_PATH)
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> findAllSection(@PathVariable Long lineId) {
        return ResponseEntity.ok(sectionService.findAllSection(lineId));
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> saveSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create(LINE_URI_PATH + "/" + lineId + "/sections")).body(sectionResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteLineOfSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
