package subway.section.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.section.dto.CreateSectionRequest;
import subway.section.service.SectionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final SectionService sectionService;
    @PostMapping
    public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody CreateSectionRequest request) {
        sectionService.addSection(lineId, request.toDto());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.removeSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
