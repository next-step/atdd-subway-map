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
  ResponseEntity<List<SubwayLineResponse>> getAllLine() {
    return ResponseEntity.ok(subwayLineService.getAllSubwayLine());
  }

  @GetMapping("/lines/{id}")
  ResponseEntity<SubwayLineResponse> getLine(@PathVariable Long id) {
    return ResponseEntity.ok(subwayLineService.getSubwayLine(id));
  }

  @PostMapping("/lines")
  ResponseEntity<SubwayLineResponse> createLine (@RequestBody LineRequest request) {
    SubwayLineResponse response = lineService.createLine(request);
    final String uri = String.format("/lines/%s", response.getId());
    return ResponseEntity.created(URI.create(uri))
        .body(response);
  }

  @PatchMapping("/lines/{id}")
  ResponseEntity<Void> editLine(@PathVariable Long id, @RequestBody SubwayLineEditRequest request) {
    subwayLineService.editSubwayLine(id, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/lines/{id}")
  ResponseEntity<Void> deleteLine(@PathVariable Long id) {
    subwayLineService.deleteSubwayLine(id);
    return ResponseEntity.noContent().build();
  }
}
