package nextstep.subway.ui;

import nextstep.subway.applicaion.SubwayLineService;
import nextstep.subway.applicaion.dto.subwayLine.CreateSubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayLine.SubwayLineResponse;
import nextstep.subway.applicaion.dto.subwayLine.UpdateSubwayLineRequest;
import nextstep.subway.domain.subwayLine.SubwayLine;
import nextstep.subway.domain.subwayLine.SubwayLineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SubwayLineController {

    private final SubwayLineRepository subwayLineRepository;

    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService, SubwayLineRepository subwayLineRepository) {
        this.subwayLineService = subwayLineService;
        this.subwayLineRepository = subwayLineRepository;
    }

    @PostMapping("/subway-lines")
    public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody CreateSubwayLineRequest subwayLineRequest) {
        final SubwayLineResponse subwayLineResponse = subwayLineService.saveSubwayLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/stations/" + subwayLineResponse.getId())).body(subwayLineResponse);
    }

    @GetMapping("/subway-lines")
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLines() {
        final List<SubwayLineResponse> subwayLinesResponse = subwayLineService.findAllSubwayLines();
        return ResponseEntity.ok().body(subwayLinesResponse);
    }

    @GetMapping("/subway-lines/{subwayLineId}")
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long subwayLineId) {
        final SubwayLineResponse subwayLineResponse = subwayLineService.findOneSubwayLineById(subwayLineId);
        return ResponseEntity.ok().body(subwayLineResponse);
    }

    @PutMapping("/subway-lines/{subwayLineId}")
    public ResponseEntity<SubwayLineResponse> updateSubwayLine(
            @PathVariable Long subwayLineId,
            @RequestBody UpdateSubwayLineRequest updateSubwayLineRequest) {
        final SubwayLineResponse subwayLineResponse = subwayLineService.updateSubwayLine(subwayLineId, updateSubwayLineRequest);
        return ResponseEntity.ok().body(subwayLineResponse);
    }

    @DeleteMapping("/subway-lines/{subwayLineId}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long subwayLineId) {
        subwayLineService.performDeleteSubwayLine(subwayLineId);
        subwayLineService.deleteSubwayLine(subwayLineId);
        return ResponseEntity.noContent().build();
    }
}
