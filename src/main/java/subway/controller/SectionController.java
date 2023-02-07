package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.SectionsResponse;
import subway.service.SectionService;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionsResponse> readSections(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(sectionService.findAllSections(lineId));
    }
}
