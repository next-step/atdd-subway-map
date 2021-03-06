package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {
  private final LineService lineService;

  public LineController(final LineService lineService) {
    this.lineService = lineService;
  }

  @PostMapping
  public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
    LineResponse line = lineService.saveLine(lineRequest);
    return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
  }

  @GetMapping
  public ResponseEntity getAllLine() {
    return ResponseEntity.ok().body(lineService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity getLine(@PathVariable long id) {
    LineResponse line = lineService.get(id);
    return ResponseEntity.ok().body(line);
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@PathVariable long id, @RequestBody LineRequest lineRequest) {
    LineResponse line = lineService.saveLine(id, lineRequest);
    return ResponseEntity.ok().body(line);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity remove(@PathVariable long id) {
    lineService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
