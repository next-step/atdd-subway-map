package nextstep.subway.section.ui;

import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.application.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineResponse> addSection(
            @PathVariable final Long lineId,
            @RequestBody final SectionRequest sectionRequest) {

        final LineResponse lineResponse = sectionService.addSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteLine(
            @PathVariable final Long lineId,
            @RequestParam final String stationId) {

        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
