package nextstep.subway.applicaion.section.ui;

import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.applicaion.section.SectionService;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        sectionService.saveSection(id, sectionRequest);
        return ResponseEntity.ok().build();
    }
}
