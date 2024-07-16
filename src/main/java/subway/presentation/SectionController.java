package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;

import java.net.URI;

@RequestMapping("/lines/{lineId}/sections")
@RestController
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity
                .created(URI.create("/lines/"+lineId+"/sections/" + sectionResponse.getSectionId()))
                .body(sectionResponse);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
