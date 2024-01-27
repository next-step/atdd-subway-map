package subway;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubwayLineController {
    private SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping("/subwayLines")
    public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse line = subwayLineService.saveLine(subwayLineRequest);

        return ResponseEntity.created(URI.create("/subwayLines/" + line.getId())).body(line);
    }
}
