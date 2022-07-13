package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.exception.AlreadyRegisteredException;
import nextstep.subway.exception.NotDownStationException;
import nextstep.subway.exception.OnlyOneSectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        try {
            sectionService.createSection(lineId, sectionRequest);
            return ResponseEntity.ok().build();
        } catch (NotDownStationException | AlreadyRegisteredException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        try {
            System.out.println(lineId + " " + stationId);
            sectionService.deleteSection(lineId, stationId);
            return ResponseEntity.noContent().build();
        } catch (NotDownStationException | OnlyOneSectionException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(sectionService.findSectionsByLineId(lineId));
    }
}
