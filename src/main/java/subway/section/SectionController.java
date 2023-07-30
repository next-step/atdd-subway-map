package subway.section;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.Line;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> makeSections(@RequestBody SectionRequest request, @PathVariable Long lineId){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sectionService.makeSections(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance()));
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> makeSections(@RequestParam("stationId") Long stationId, @PathVariable Long lineId){
        sectionService.delete(lineId, stationId);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).build();
    }
}
