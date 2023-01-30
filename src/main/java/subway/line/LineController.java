package subway.line;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

  private final LineService lineService;

  public LineController(LineService lineService) {
    this.lineService = lineService;
  }

  @PostMapping("/lines")
  public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
    return ResponseEntity.ok().body(lineService.saveLine(request));
  }

  @GetMapping("/lines/{id}")
  public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
    LineResponse response = lineService.showLine(id);
    if (response == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/lines")
  public ResponseEntity<List<LineResponse>> showAllLines() {
    return ResponseEntity.ok().body(lineService.showLines());
  }

  @PatchMapping("/lines/{id}")
  public ResponseEntity<LineResponse> patchLine(@PathVariable Long id, @RequestBody LineRequest request) {
    LineResponse response = lineService.updateLine(id,request);
    if (response == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/lines/{id}")
  public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
    lineService.deleteLineById(id);
    return ResponseEntity.noContent().build();
  }
}
