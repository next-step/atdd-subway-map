package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.SectionRequest;

import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) { this.lineService = lineService; }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse linerResponse = lineService.createLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + linerResponse.getId())).body(linerResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showSubwayLines() {
        return ResponseEntity.ok().body(lineService.findAllSubwayLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findSubwayLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubwayLine(@PathVariable Long id
            , @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateSubwayLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LineResponse> deleteSubwayLine(@PathVariable Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId,
                                                               @RequestParam("stationId") Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}