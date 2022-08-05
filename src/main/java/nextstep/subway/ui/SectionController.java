package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.section.CreateSectionRequest;
import nextstep.subway.applicaion.dto.section.SectionResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(
            @PathVariable Long lineId,
            @RequestBody CreateSectionRequest createSectionRequest) {
        final Long sectionId = sectionService.create(lineId, createSectionRequest);
        final Section findSection = sectionService.findOne(sectionId);
        final SectionResponse sectionResponse = new SectionResponse(findSection);

        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections")).body(sectionResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.delete(lineId, stationId);

        return ResponseEntity.noContent().build();
    }
}
