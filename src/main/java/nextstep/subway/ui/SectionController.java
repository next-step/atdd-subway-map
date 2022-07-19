package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SectionController {

    public SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<SectionResponse> registSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) throws Exception {
        sectionService.checkRegistPreCodition(id, sectionRequest);
        SectionResponse response = sectionService.saveSection(id, sectionRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) throws Exception {
        sectionService.checkDeletePreCondition(id, stationId);
        sectionService.deleteByStationId(stationId);
        return ResponseEntity.noContent().build();
    }

}
