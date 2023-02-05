package subway.section;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SectionController {

  private final SectionService sectionService;

  public SectionController(SectionService sectionService) {
    this.sectionService = sectionService;
  }

  @PostMapping("/lines/{id}/sections")
  public void createSection(@PathVariable Long id, @RequestBody SectionCreateRequest request) {
    sectionService.createSection(id, request);
  }

  @DeleteMapping("/lines/{lineId}/sections")
  public void removeSection(@RequestParam Long id, @PathVariable Long lineId) {
    sectionService.removeSection();
  }
}
