package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.LineResponse;
import subway.section.SectionService;
import subway.section.dto.SectionDto;
import subway.section.dto.SectionRequest;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineSectionController {

    private SectionService sectionService;

    public LineSectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> addLineSection(
            @PathVariable Long lineId,
            @RequestBody SectionRequest lineSectionRequest
    ) {
        var lineResponse = sectionService.addSection(lineId, SectionDto.from(lineSectionRequest));
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteLineSection(
            @PathVariable Long lineId,
            @RequestParam("stationId") Long stationId
    ) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
