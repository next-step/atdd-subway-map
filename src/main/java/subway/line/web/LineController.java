package subway.line.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService)
    {
        this.lineService = lineService;
    }

    @PostMapping("/line")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {

        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/line/" + line.getId())).body(line);
    }

    @GetMapping("/line")
    public ResponseEntity<List<LineResponse>> showLines() {

        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/line/{id}")
    public ResponseEntity<LineResponse> showLineById(@PathVariable long id) {

        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @PutMapping("/line/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable long id, @RequestBody LineRequest lineRequest) {

        return ResponseEntity.ok().body(lineService.updateLine(id, lineRequest));
    }

    @DeleteMapping("/line/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable long id) {

        lineService.deleteLine(id);

        return ResponseEntity.noContent().build();
    }
}
