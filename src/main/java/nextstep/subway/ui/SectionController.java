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
    public ResponseEntity<SectionResponse> registSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        boolean preCondition = sectionService.checkRegistPreCodition(id, sectionRequest);
        if(!preCondition){
            return ResponseEntity.internalServerError().build();
        }
        SectionResponse response = sectionService.saveSection(id, sectionRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        boolean preCondition = sectionService.checkDeletePreCondition(id, stationId);
        if(!preCondition){
            return ResponseEntity.internalServerError().build();
        }
        sectionService.deleteByStationId(stationId);
        return ResponseEntity.noContent().build();
    }

}
