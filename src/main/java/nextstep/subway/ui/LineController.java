package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineCommandService;
import nextstep.subway.applicaion.LineQueryService;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineQueryService lineQueryService;
    private final LineCommandService lineCommandService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        LineResponse line = lineCommandService.saveLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineQueryService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineQueryService.findLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineCommandService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineCommandService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        lineCommandService.addSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections")).build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam("stationId") Long stationId) {
        lineCommandService.deleteLastSection(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
