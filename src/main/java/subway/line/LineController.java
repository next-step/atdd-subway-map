package subway.line;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LineController {

  private final SubwayLineService subwayLineService;
  private final LineService lineService;

  @GetMapping("/lines")
  public ResponseEntity<List<LineResponse>> getAllLine() {
    return ResponseEntity.ok(subwayLineService.getAllSubwayLine());
  }

  @GetMapping("/lines/{id}")
  public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
    return ResponseEntity.ok(lineService.getSubwayLine(id));
  }

  @PostMapping("/lines")
  public ResponseEntity<LineResponse> createLine (@RequestBody SubwayLineRequest request) {
    LineResponse response = lineService.createLine(request);
    final String uri = String.format("/lines/%s", response.getId());
    return ResponseEntity.created(URI.create(uri))
        .body(response);
  }

  @PatchMapping("/lines/{id}")
  public ResponseEntity<Void> editLine(@PathVariable Long id, @RequestBody SubwayLineEditRequest request) {
    subwayLineService.editSubwayLine(id, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/lines/{id}")
  public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
    lineService.deleteLine(id);
    return ResponseEntity.noContent().build();
  }
}
