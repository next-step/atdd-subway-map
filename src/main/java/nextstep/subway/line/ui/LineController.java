package nextstep.subway.line.ui;

import nextstep.subway.exceptions.AlreadyExistsEntityException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity showLines() {
        List<LineResponse> lines = lineService.readLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping("/{id}")
    public ResponseEntity showLine(@PathVariable Long id) {
        LineResponse line = lineService.readLine(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AlreadyExistsEntityException.class)
    public ResponseEntity alreadyExistsEntityException(AlreadyExistsEntityException e) {
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Line line = lineService.addLineStation(lineId, sectionRequest);
        Long lastedSectionId = line.getLastSection().getId();
        LineResponse response = LineResponse.of(line);

        return ResponseEntity.created(URI.create(String.format("/lines/%d/sections/%d", lineId, lastedSectionId))).body(response);
    }
}
