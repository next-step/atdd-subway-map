package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.section.SectionRequest;
import subway.service.SectionService;

@RestController
@RequestMapping("/lines/{id}/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Long> createSection(
        @PathVariable("id") Long lineId,
        @RequestBody SectionRequest sectionRequest
    ) {
        Long sectionId = sectionService.createSection(lineId, sectionRequest);

        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(sectionId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(
        @PathVariable("id") Long lineId,
        @RequestParam Long stationId
    ) {
        sectionService.deleteSection(lineId, stationId);
    }
}
