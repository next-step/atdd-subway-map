package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse response = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineResponse response = lineService.findById(id);
        return ResponseEntity.ok().body(response);
    }
}
