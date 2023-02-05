package subway.section.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.dto.SectionSaveRequest;
import subway.section.service.SectionService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @RequestBody SectionSaveRequest saveRequest) {
        sectionService.saveSection(lineId, saveRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections")).build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        return ResponseEntity.noContent().build();
    }

}