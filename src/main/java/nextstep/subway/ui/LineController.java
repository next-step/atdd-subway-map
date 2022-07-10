package nextstep.subway.ui;

import java.net.URI;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.line.LineCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

    private final LineCreator lineCreator;

    public LineController(LineCreator lineCreator) {
        this.lineCreator = lineCreator;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreationRequest request) {
        var line = lineCreator.create(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }
}
