package nextstep.subway.ui;

import nextstep.subway.applicaion.SubwayLineService;
import nextstep.subway.applicaion.dto.SubwayLineModifyRequest;
import nextstep.subway.applicaion.dto.SubwayLineResponse;
import nextstep.subway.applicaion.dto.SubwayLineSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SubwayLineController {

    public static final String SUBWAY_LINE_URI = "/lines";
    public static final String SUBWAY_LINE_BY_ID_URI = "/lines/{id}";

    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @GetMapping(SUBWAY_LINE_URI)
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLines() {
        return ResponseEntity.ok(subwayLineService.getSubwayLines());
    }

    @GetMapping(SUBWAY_LINE_BY_ID_URI)
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok(subwayLineService.getSubwayLineById(id));
    }

    @PostMapping(SUBWAY_LINE_URI)
    public ResponseEntity<SubwayLineResponse> saveSubwayLine(@RequestBody SubwayLineSaveRequest subwayLineSaveRequest) {
        SubwayLineResponse response = subwayLineService.saveSubwayLine(subwayLineSaveRequest);
        return ResponseEntity.created(URI.create(SUBWAY_LINE_URI + response.getId())).body(response);
    }

    @PutMapping(SUBWAY_LINE_BY_ID_URI)
    public ResponseEntity<Void> modifySubwayLine(@PathVariable Long id, @RequestBody SubwayLineModifyRequest subwayLineModifyRequest) {
        subwayLineService.modifySubwayLine(id, subwayLineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(SUBWAY_LINE_BY_ID_URI)
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLine(id);
        return ResponseEntity.noContent().build();
    }
}
