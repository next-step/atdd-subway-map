package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SubwayLineController {

    private final SubwayLineService lineService;

    public SubwayLineController(SubwayLineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<SubwayLineResponse> createStation(@RequestBody SubwayLineRequest lineRequest) {
        SubwayLineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }


    @GetMapping("/lines")
    public ResponseEntity<List<SubwayLineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<SubwayLineResponse> showLine(@PathVariable("id") Long lineId) {
        return ResponseEntity.ok().body(lineService.findByLineId(lineId));
    }
}
