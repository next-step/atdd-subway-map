package subway.section.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.section.application.SectionService;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;

@RequestMapping("/lines/{lineId}")
@RestController
public class SectionController {  //TODO 해당 API는 lines의 하위에 있다고 볼 수 있는데, 이렇게 분리하는게 맞는지, LineController에서 하는게 맞는지
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public ResponseEntity<SectionResponse> registerSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        return ResponseEntity.ok().body(sectionService.registerSection(lineId, sectionRequest));
    }

    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
