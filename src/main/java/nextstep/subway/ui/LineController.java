package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.line.LineCreator;
import nextstep.subway.applicaion.line.LineQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

    private final LineCreator lineCreator;
    private final LineQueryService lineQueryService;

    public LineController(
            LineCreator lineCreator,
            LineQueryService lineQueryService
    ) {
        this.lineCreator = lineCreator;
        this.lineQueryService = lineQueryService;
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        var lines = lineQueryService.getAllLines();
        return ResponseEntity.ok(lines);
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreationRequest request) {
        var line = lineCreator.create(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }
}
