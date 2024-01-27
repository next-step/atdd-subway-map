package subway;

import java.net.URI;
import java.util.List;
import javax.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/subwayLines")
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLineList() {
        return ResponseEntity.ok().body(subwayLineService.findAllLines());
    }

    @GetMapping("/subwayLines/{id}")
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.findLine(id));
    }
}
