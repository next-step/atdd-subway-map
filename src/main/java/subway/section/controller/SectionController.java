package subway.section.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.domain.Section;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.service.SectionCreationService;
import subway.section.service.SectionDeletionService;

import java.net.URI;

@RestController
public class SectionController {

    private final SectionCreationService sectionService;
    private final SectionDeletionService sectionDeletionService;

    public SectionController(SectionCreationService sectionService, SectionDeletionService sectionDeletionService) {
        this.sectionService = sectionService;
        this.sectionDeletionService = sectionDeletionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest request){
        Section savedSection = sectionService.createSection(lineId, request);
        SectionResponse sectionResponse = SectionResponse.fromEntity(savedSection);
        return ResponseEntity.created(URI.create("/sections/" + sectionResponse.getId())).body(sectionResponse);
    }

    @GetMapping("/sections/{id}")
    public ResponseEntity<SectionResponse> getSection(@PathVariable Long id){
        Section savedSection = sectionService.getSection(id);
        SectionResponse sectionResponse = SectionResponse.fromEntity(savedSection);
        return ResponseEntity.ok(sectionResponse);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<?> deleteSection(@PathVariable Long lineId, SectionRequest.StationIdParams queryParams){
        sectionDeletionService.deleteSection(lineId, queryParams.getStationId());
        return ResponseEntity.noContent().build();
    }
}
