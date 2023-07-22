package subway.section.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.domain.Section;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.service.SectionCreationService;

import java.net.URI;

@RestController
public class SectionController {

    private final SectionCreationService sectionService;

    public SectionController(SectionCreationService sectionService) {
        this.sectionService = sectionService;
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
}
