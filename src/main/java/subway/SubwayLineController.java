package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
