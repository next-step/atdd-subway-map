package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionCreateResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionsResponse;
import subway.service.SectionService;

import java.net.URI;

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

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<SectionCreateResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
        SectionCreateResponse response = sectionService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/"+lineId + "/sections/" + response.getLineId())).body(response);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
