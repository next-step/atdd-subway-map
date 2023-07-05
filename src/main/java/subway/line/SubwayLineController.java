package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RestController
public class SubwayLineController {

    private SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) { this.subwayLineService = subwayLineService; }

    @PostMapping("/subway-lines")
    public ResponseEntity<SubwayLineResponse> createLine(@RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse line = subwayLineService.createLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/subway-lines/" + line.getId())).body(line);
    }

    @GetMapping("/subway-lines")
    public ResponseEntity<List<SubwayLineResponse>> showSubwayLines() {
        return ResponseEntity.ok().body(subwayLineService.findAllSubwayLines());
    }

    @GetMapping("/subway-lines/{id}")
    public ResponseEntity<SubwayLineResponse> showSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.findSubwayLine(id));
    }

    @PutMapping("/subway-lines/{id}")
    public ResponseEntity<Void> updateSubwayLine(@PathVariable Long id
            , @RequestBody SubwayLineUpdateRequest subwayLineUpdateRequest) {
        subwayLineService.updateSubwayLine(id, subwayLineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/subway-lines/{id}")
    public ResponseEntity<SubwayLineResponse> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}