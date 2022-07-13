package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
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

    @GetMapping(path = "/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable long lineId) {
        List<SectionResponse> sections = sectionService.getSections(lineId);
        return ResponseEntity.ok().body(sections);
    }

    @PostMapping(path = "/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> registerSection(@PathVariable long lineId,
            @RequestBody SectionRequest sectionRequest) {

        SectionResponse section = sectionService.registerSection(lineId,sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections" + section.getId())).body(section);
    }
}
