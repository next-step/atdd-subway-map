package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping(value = "/{id}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable("id") Long lineId, @RequestBody SectionRequest request) {
        SectionResponse response = sectionService.saveSection(lineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(value = "/{id}/sections")
    public ResponseEntity<Void> createSection(@PathVariable("id") Long lineId, @RequestParam Long stationId) {
        sectionService.deleteStationById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
