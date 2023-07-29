package subway.section.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.dto.CreateSectionRequest;
import subway.section.dto.CreateSectionResponse;
import subway.section.dto.SectionResponse;
import subway.section.service.SectionService;

import java.util.List;

@RequestMapping("/lines/{lineId}/sections")
@RestController
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<CreateSectionResponse> createSection(@PathVariable Long lineId, @RequestBody CreateSectionRequest createSectionRequest
    ) {
        CreateSectionResponse createSectionResponse = sectionService.createSection(lineId, createSectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createSectionResponse);
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> findSections(@PathVariable Long lineId) {
        List<SectionResponse> sections = sectionService.findSections(lineId);
        return ResponseEntity.ok(sections);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
