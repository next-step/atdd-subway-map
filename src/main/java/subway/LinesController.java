package subway;


import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinesController {
    private LinesService linesService;

    public LinesController(LinesService linesService) {
        this.linesService = linesService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LinesResponse> createLines(@RequestBody LinesCreateRequest linesCreateRequest) {
        LinesResponse lines = linesService.saveLines(linesCreateRequest);

        return ResponseEntity.created(URI.create("/lines/" + lines.getId())).body(lines);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LinesResponse>> getLinesList() {
        return ResponseEntity.ok().body(linesService.getLinesList());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LinesResponse> getLines(@PathVariable Long id) {
        return ResponseEntity.ok().body(linesService.getLines(id));
    }

}
