package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineCreateService lineCreateService;

    public LineController(LineCreateService lineCreateService) {
        this.lineCreateService = lineCreateService;
    }

    @PostMapping
    public ResponseEntity<LineCreatedResponse> createLine(@RequestBody LineCreateRequest request) {
        LineCreatedResponse response = lineCreateService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
}
