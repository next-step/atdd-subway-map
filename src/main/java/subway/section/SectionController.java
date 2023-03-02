package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import subway.line.LineService;

@Controller
public class SectionController {

  private final LineService lineService;

  public SectionController(LineService lineService) {
    this.lineService = lineService;
  }

  @PostMapping("/lines/{id}/sections")
  public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @RequestBody SectionCreateRequest request) {
    return ResponseEntity.ok().body(lineService.createSection(id, request));
  }

  @DeleteMapping("/lines/{lineId}/sections")
  public ResponseEntity<Void> removeSection(@PathVariable Long lineId, @RequestParam Long stationId) {
    lineService.removeSection(lineId, stationId);
    return ResponseEntity.noContent().build();
  }
}
