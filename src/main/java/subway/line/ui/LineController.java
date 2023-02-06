package subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.LineService;
import subway.line.application.dto.LineCreateRequest;
import subway.line.application.dto.LineResponse;
import subway.line.application.dto.LineUpdateRequest;
import subway.line.application.dto.SectionAddRequest;

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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest request) {
        final LineResponse lineResponse = lineService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
            .body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAll() {
        final List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        final LineResponse lineResponse = lineService.findLineById(id);
        return ResponseEntity.ok(lineResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> update(@PathVariable Long id, @RequestBody LineUpdateRequest request) {
        final LineResponse lineResponse = lineService.updateLine(id, request);
        return ResponseEntity.ok(lineResponse);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> delete(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable long lineId, @RequestBody SectionAddRequest request) {
        lineService.addSection(lineId, request);
        return ResponseEntity.ok().build();
    }

}
