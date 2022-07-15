package nextstep.subway.ui;

import java.net.URI;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionCreateRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<Object> addSections(@PathVariable final Long id,
        @RequestBody final SectionCreateRequest request) {
        SectionResponse sectionResponse = sectionService.addSections(id, request);
        return ResponseEntity.created(URI.create("/lines/" + sectionResponse.getLineId() + "/sections")).body(sectionResponse);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Object> deleteSections(@PathVariable final Long id,
        @RequestParam final long stationId) {
        sectionService.removeSections(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
