package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.section.SectionSaveRequest;
import subway.service.SectionCompositeService;

import java.net.URI;
import java.util.List;

@RestController
public class SectionController {
    private final SectionCompositeService sectionCompositeService;

    public SectionController(SectionCompositeService sectionCompositeService) {
        this.sectionCompositeService = sectionCompositeService;
    }

    @GetMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok()
                             .body(sectionCompositeService.findSectionsByLine(lineId));
    }

    @GetMapping("/sections/{sectionId}")
    public ResponseEntity<SectionResponse> getSection(@PathVariable("sectionId") Long sectionId) {
        return ResponseEntity.ok()
                             .body(sectionCompositeService.findById(sectionId));
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable("lineId") Long lineId, @RequestBody SectionSaveRequest sectionSaveRequest) {
        SectionResponse section = sectionCompositeService.saveSection(lineId, sectionSaveRequest);
        return ResponseEntity.created(URI.create("/sections/" + section.getId()))
                             .body(section);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable("lineId") Long lineId, @RequestParam("stationId") Long stationId) {
        sectionCompositeService.deleteSectionByStationId(lineId, stationId);
        return ResponseEntity.noContent()
                             .build();
    }
}
