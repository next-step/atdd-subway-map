package nextstep.subway.ui.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        final LineResponse line = lineService.saveLine(lineRequest);

        return ResponseEntity
            .created(getCreateStatusHeader(line))
            .body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getAllLines() {
        final List<LineResponse> allLines = lineService.getAllLines();
        return ResponseEntity
            .ok()
            .body(allLines);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
        final LineResponse line = lineService.getLine(id);
        return ResponseEntity
            .ok()
            .body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable long id, @RequestBody LineRequest lineRequest) {
        final LineResponse line = lineService.updateLine(id, lineRequest);
        return ResponseEntity
            .ok()
            .body(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable long id) {
        lineService.deleteLine(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    private URI getCreateStatusHeader(LineResponse line) {
        return URI.create("/lines/" + line.getId());
    }

}
