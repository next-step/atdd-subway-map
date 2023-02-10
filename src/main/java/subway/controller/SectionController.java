package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.service.SectionService;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> saveSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
        sectionService.saveSection(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
