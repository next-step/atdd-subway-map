package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = "/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse savedLine = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + savedLine.getId())).body(savedLine);
    }

    @GetMapping(path = "/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping(path = "/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
        LineResponse line = lineService.getLine(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping(path = "/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(lineRequest, id);
        return ResponseEntity.ok().body(null);
    }
}
