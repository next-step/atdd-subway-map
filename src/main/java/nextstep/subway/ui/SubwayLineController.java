package nextstep.subway.ui;

import nextstep.subway.applicaion.SubwayLineService;
import nextstep.subway.applicaion.dto.*;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SubwayLineController {

    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @GetMapping("/lines")
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLines() {
        return ResponseEntity.ok(subwayLineService.getSubwayLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok(subwayLineService.getSubwayLineById(id));
    }

    @PostMapping("/lines")
    public ResponseEntity<SubwayLineResponse> saveSubwayLine(@RequestBody SubwayLineSaveRequest subwayLineSaveRequest) {
        SubwayLineResponse response = subwayLineService.saveSubwayLine(subwayLineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> modifySubwayLine(@PathVariable Long id, @RequestBody SubwayLineModifyRequest subwayLineModifyRequest) {
        subwayLineService.modifySubwayLine(id, subwayLineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<Void> saveSubwaySection(@PathVariable Long id, @RequestBody SubwaySectionRequest subwaySectionRequest) {
        subwayLineService.saveSubwaySection(id, subwaySectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> deleteSubwaySection(@PathVariable("id") Long lineId, @RequestParam Long stationId) {
        subwayLineService.deleteSubwaySection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
