package nextstep.subway.ui;

import java.util.List;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping(path = "/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable long lineId) {
        List<SectionResponse> sections = sectionService.getSections(lineId);
        return ResponseEntity.ok().body(sections);
    }
}
