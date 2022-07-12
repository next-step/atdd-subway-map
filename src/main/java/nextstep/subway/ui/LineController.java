package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

  private final LineService lineService;

  public LineController(LineService lineService) {
    this.lineService = lineService;
  }

  @PostMapping("/lines")
  public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
    LineResponse response = lineService.createLine(lineRequest);
    return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
  }

  @GetMapping("/lines")
  public ResponseEntity<List<LineResponse>> getAllLine() {
    return ResponseEntity.ok().body(lineService.getAllLine());
  }

  @GetMapping("/lines/{id}")
  public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
    return ResponseEntity.ok().body(lineService.getLine(id));
  }

  @PutMapping("/lines/{id}")
  public ResponseEntity<Void> updateLine(@PathVariable Long id, LineUpdateRequest lineUpdateRequest) {
    lineService.updateLine(id, lineUpdateRequest);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/lines/{id}")
  public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
    lineService.deleteLine(id);
    return ResponseEntity.noContent().build();
  }
}
