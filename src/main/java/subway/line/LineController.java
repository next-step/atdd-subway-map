package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.createLine(LineDto.from(lineRequest));
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLineById(
            @PathVariable long lineId
    ) {
        LineResponse lineResponse = lineService.findLineById(lineId);
        return ResponseEntity.ok().body(lineResponse);
    }
}
