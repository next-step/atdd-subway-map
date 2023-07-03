package subway.subwayline;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class SubwayLineController {

    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping
    public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody CreateSubwayLineRequest request) {
        SubwayLineResponse response = SubwayLineResponse.from(subwayLineService.createSubwayLine(request.toDto()));
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLines() {
        List<SubwayLineResponse> response = subwayLineService.getSubwayLines().stream().map(SubwayLineResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(SubwayLineResponse.from(subwayLineService.getSubwayLine(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifySubwayLine(@PathVariable Long id, @RequestBody ModifySubwayLineRequest request) {
        subwayLineService.modifySubwayLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLine(id);
        return ResponseEntity.noContent().build();
    }
}
