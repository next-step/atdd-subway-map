package nextstep.subway.ui;

import java.util.List;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLine(lineId));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        return ResponseEntity.ok(lineService.updateLine(lineId, lineRequest));
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<LineResponse> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
