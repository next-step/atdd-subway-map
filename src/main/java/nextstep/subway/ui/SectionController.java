package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId,
                                                         @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = sectionService.saveSection(sectionRequest, lineId);
        return ResponseEntity.created(URI.create("lines/" + lineId + "/sections/" + section.getId()))
                .body(section);
    }
}
