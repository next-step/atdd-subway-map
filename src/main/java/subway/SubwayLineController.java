package subway;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubwayLineController {

    private final SubwayLineService subwayLineService;

    @PostMapping("/lines")
    public ResponseEntity<SubwayLineResponse> createLine(
        @RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse subwayLineResponse = subwayLineService.saveLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + subwayLineResponse.getId()))
            .body(subwayLineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLineList() {
        return ResponseEntity.ok().body(subwayLineService.getSubwayLineList());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.getSubwayLine(id));
    }

    @PutMapping("/lines/{id}")
    public void modifySubwayLine(@PathVariable Long id,
        @RequestBody SubwayLineRequest subwayLineRequest) {
        subwayLineService.updateSubwayLine(id, subwayLineRequest);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLine(id);
        return ResponseEntity.noContent().build();
    }


}
