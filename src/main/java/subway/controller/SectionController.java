package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.response.LineResponse;
import subway.dto.request.SectionRequest;
import subway.service.SectionService;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private SectionService sectionService;

    public SectionController(SectionService serctionService) {
        this.sectionService = serctionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId){
        sectionService.removeSection(lineId,stationId);
        return ResponseEntity.noContent().build();
    }
}
