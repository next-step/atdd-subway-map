package nextstep.subway.ui;

import nextstep.subway.applicaion.SubwayLineService;
import nextstep.subway.applicaion.dto.subwayLine.CreateSubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayLine.SubwayLineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class SubwayLineController {

    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping("/subway-lines")
    public ResponseEntity<SubwayLineResponse> createStation(@RequestBody CreateSubwayLineRequest subwayLineRequest) {
        final SubwayLineResponse subwayLineResponse = subwayLineService.saveSubwayLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/stations/" + subwayLineResponse.getId())).body(subwayLineResponse);
    }

    @GetMapping("/subway-lines")
    public ResponseEntity<List<SubwayLineResponse>> getStations() {
        final List<SubwayLineResponse> subwayLinesResponse = subwayLineService.findAllSubwayLines();
        return ResponseEntity.ok().body(subwayLinesResponse);
    }
}
