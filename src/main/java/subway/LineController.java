package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<BuildLineResponse> buildLine(@RequestBody BuildLineRequest request) {
        BuildLineResponse response = lineService.addLine(request.getName());
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
}
