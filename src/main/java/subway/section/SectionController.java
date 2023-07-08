package subway.section;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/lines/{lineId}/sections")
@RestController
public class SectionController {

  private final SectionService sectionService;

  public SectionController(SectionService sectionService) {
    this.sectionService = sectionService;
  }

  @PostMapping
  public SectionResponse createSection(@PathVariable Long lineId, SectionRequest request) {
    return sectionService.createSection(request);
  }
}
