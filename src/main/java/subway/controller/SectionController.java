package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.section.SectionSaveRequest;
import subway.service.LineService;
import subway.service.SectionService;

import java.net.URI;
import java.util.List;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> showSections(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok()
                             .body(sectionService.findSectionsByLine(lineId));
    }

    @GetMapping("/sections/{sectionId}")
    public ResponseEntity<SectionResponse> getLine(@PathVariable("sectionId") Long sectionId) {
        return ResponseEntity.ok()
                             .body(sectionService.findById(sectionId));
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createLine(@PathVariable("lineId") Long lineId, @RequestBody SectionSaveRequest sectionSaveRequest) {
        SectionResponse section = sectionService.saveSection(lineId, sectionSaveRequest);
        return ResponseEntity.created(URI.create("/sections/" + section.getId()))
                             .body(section);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteLine(@PathVariable("lineId") Long lineId, @RequestParam("stationId") Long stationId) {
        sectionService.deleteSectionByStationId(lineId, stationId);
        return ResponseEntity.noContent()
                             .build();
    }
}
