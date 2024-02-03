package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.SectionCreateRequest;
import subway.service.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable Long lineId,
            @Valid @RequestBody SectionCreateRequest request) {
        Long sectionId = sectionService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/stations/" + sectionId)).build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
