package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class SectionController {

    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest sectionRequest, @PathVariable long lineId) {
        try {
            SectionResponse section = sectionService.saveSection(sectionRequest, lineId);
            return ResponseEntity.created(URI.create("/sections/" + section.getId())).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
