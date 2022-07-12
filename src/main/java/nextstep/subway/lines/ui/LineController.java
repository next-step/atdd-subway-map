package nextstep.subway.lines.ui;

import nextstep.subway.lines.application.LineService;
import nextstep.subway.lines.application.dto.LineResponse;
import nextstep.subway.lines.application.dto.LineSaveRequest;
import nextstep.subway.lines.application.dto.LineSaveResponse;
import nextstep.subway.lines.application.dto.LineUpdateRequest;
import nextstep.subway.lines.domain.Line;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(value = "/lines")
    public ResponseEntity<LineSaveResponse> saveLine(@RequestBody LineSaveRequest lineSaveRequest) {
        Line line = lineService.saveLine(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(LineSaveResponse.of(line));
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok(lineService.getLines().stream()
                .map(LineResponse::new)
                .collect(toList()));
    }

    @GetMapping(value = "/lines/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId) {
        return ResponseEntity.ok(new LineResponse(lineService.getLine(lineId)));
    }

    @PutMapping(value = "/lines/{lineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId,
                                           @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
