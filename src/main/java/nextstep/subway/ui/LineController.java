package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineWithStationResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineWithStationResponse>> getLines() {
        List<LineWithStationResponse> lines = lineService.getLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineWithStationResponse> getLine(@PathVariable Long id) {
        LineWithStationResponse line = lineService.getLine(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.editLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> saveSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        lineService.createSection(id, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
