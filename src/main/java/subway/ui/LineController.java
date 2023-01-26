package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.domain.LineRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        final LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
                .body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        final List<LineResponse> lineResponses = lineService.findAllLine();
        return ResponseEntity.ok(lineResponses);
    }
}
