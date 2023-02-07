package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LineResponse> patchLine(@RequestBody LineRequest lineRequest,
                                                  @PathVariable Long id) {
        LineResponse lineResponse = lineService.patchLine(id, lineRequest);
        return ResponseEntity.ok().body(lineResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> appendSection(@PathVariable Long lineId,
                                              @RequestBody SectionRequest sectionRequest) {

        return ResponseEntity.ok().body(lineService.appendSection(lineId, sectionRequest));
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId,
                                              @RequestParam(value = "stationId") Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lineId}/sections/{sectionId}")
    public ResponseEntity<SectionResponse> getSection(@PathVariable Long lineId,
                                                      @PathVariable Long sectionId) {
        SectionResponse sectionResponse
                = lineService.findSectionByLineIdAndSectionId(lineId, sectionId);
        return ResponseEntity.ok().body(sectionResponse);
    }
}
