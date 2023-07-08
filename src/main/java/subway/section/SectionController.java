package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lines/{lineId}/sections")
@RestController
public class SectionController {

  private final SectionService sectionService;

  public SectionController(SectionService sectionService) {
    this.sectionService = sectionService;
  }

  @PostMapping
  public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
    return ResponseEntity.ok(sectionService.createSection(lineId, request));
  }

  @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
