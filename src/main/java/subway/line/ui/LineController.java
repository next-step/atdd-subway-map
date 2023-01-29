package subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.service.LineService;
import subway.line.service.dto.LineCreateRequest;
import subway.line.service.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> create(@RequestBody LineCreateRequest request) {
        final LineResponse lineResponse = lineService.create(request);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
            .body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAll() {
        final List<LineResponse> lineResponses = lineService.findAll();
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        final LineResponse lineResponses = lineService.findById(id);
        return ResponseEntity.ok(lineResponses);
    }

}
