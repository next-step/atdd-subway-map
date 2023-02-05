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
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(
        @RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
            .body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getSubwayLineList() {
        return ResponseEntity.ok().body(lineService.getSubwayLineList());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.getSubwayLine(id));
    }

    @PutMapping("/lines/{id}")
    public void modifySubwayLine(@PathVariable Long id,
        @RequestBody LineRequest lineRequest) {
        lineService.updateSubwayLine(id, lineRequest);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        lineService.deleteSubwayLine(id);
        return ResponseEntity.noContent().build();
    }


}
