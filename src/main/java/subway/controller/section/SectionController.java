package subway.controller.section;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.line.LineResponse;
import subway.dto.section.SectionRequest;
import subway.service.section.SectionService;

import java.net.URI;


@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {

        LineResponse line = sectionService.saveSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId() + "/sections")).body(line);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> deleteSection(@PathVariable Long id, @RequestParam("stationId") Long stationId ) {
        LineResponse line = sectionService.deleteSection(id, stationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(line);

    }


}
