package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.ShowLineResponse;
import nextstep.subway.applicaion.dto.UpdateLineRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<ShowLineResponse>> showLine() {
        List<ShowLineResponse> lines = lineService.findAllLines();

        return ResponseEntity.ok(lines);
    }

    @GetMapping("{id}")
    public ResponseEntity<ShowLineResponse> showLine(@PathVariable("id") Long id) {
        ShowLineResponse line = lineService.findLine(id);

        return ResponseEntity.ok(line);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateLine(@PathVariable("id") Long id,
                                           @RequestBody UpdateLineRequest request) {
        lineService.updateLine(id, request);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable("id") Long id) {
        lineService.deleteLine(id);

        return ResponseEntity.noContent()
                .build();
    }

}
