package subway;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

}
