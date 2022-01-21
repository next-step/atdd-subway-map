package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.ShowLineResponse;
import nextstep.subway.applicaion.dto.UpdateLineRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines")
public class LineController {

    private LineService lineService;

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

    @PutMapping("{id}")
    public ResponseEntity<Void> updateLine(
        @PathVariable("id") Long id,
        @RequestBody UpdateLineRequest request) {

        lineService.updateLine(id, request);

        return ResponseEntity.noContent()
            .build();
    }

}
