package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SectionController {

    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<SectionResponse> createLine(@PathVariable Long id, @RequestBody SectionRequest request) {
        SectionResponse response = sectionService.save(id, request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> createLine(@PathVariable Long id, @RequestParam Long stationId) {
        sectionService.delete(id, stationId);
        return ResponseEntity.noContent().build();
    }

}
