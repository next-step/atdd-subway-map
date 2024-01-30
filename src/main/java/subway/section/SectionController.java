package subway.section;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createLine(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        final SectionResponse sectionResponse = sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionResponse);
    }
}
