package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineService lineService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        log.debug("request create line, line name is[{}]", lineRequest.getName());
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        log.debug("request find lines");
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        log.debug("request find line, id is {}", id);
        return ResponseEntity.ok().body(lineService.findOneLine(id));
    }

    @PostMapping("/lines/{id}")
    public ResponseEntity<LineResponse> editLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        log.debug("request edit line, id is {} and required change name {}", id, lineRequest.getName());
        return ResponseEntity.ok().body(lineService.editLine(id, lineRequest));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        log.debug("request delete line, id is {}", id);
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

}
