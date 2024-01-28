package subway.line;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines")
public class LineController {

  private final LineService lineService;

  public LineController(LineService lineService) {
    this.lineService = lineService;
  }

  @PostMapping
  public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest request) {
    final var response = lineService.saveLine(request);
    return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
  }

  @GetMapping
  public ResponseEntity<List<LineResponse>> getLines() {
    return ResponseEntity.ok().body(lineService.findAllLines());
  }

  @GetMapping("/{id}")
  public ResponseEntity<LineResponse> getLines(@PathVariable Long id) {
    return ResponseEntity.ok().body(lineService.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> getLines(@PathVariable Long id, @RequestBody LineUpdateRequest request) {
    lineService.updateLine(id, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
    lineService.deleteLineById(id);
    return ResponseEntity.noContent().build();
  }

}
