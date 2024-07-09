package subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubwayLineController {
    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping("/lines")
    ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest request) {
        var response = subwayLineService.saveSubwayLine(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/lines")
    ResponseEntity<List<SubwayLineResponse>> showSubwayLines() {
        var response = subwayLineService.findAllSubwayLines();
        return ResponseEntity.ok(response);
    }

}
